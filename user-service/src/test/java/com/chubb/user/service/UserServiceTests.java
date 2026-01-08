package com.chubb.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.chubb.user.dto.request.LoginRequest;
import com.chubb.user.dto.request.RegisterRequest;
import com.chubb.user.dto.response.AuthResponse;
import com.chubb.user.dto.response.UserResponse;
import com.chubb.user.entity.Role;
import com.chubb.user.entity.User;
import com.chubb.user.exception.BusinessException;
import com.chubb.user.repository.UserRepository;
import com.chubb.user.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository repo;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerReq;

    @BeforeEach
    void setup() {
        registerReq = new RegisterRequest();
        registerReq.setUsername("u1");
        registerReq.setEmail("u1@example.com");
        registerReq.setPassword("pwd");
        registerReq.setRole("USER");
    }

    @Test
    void register_success() {
        when(repo.findByUsername("u1")).thenReturn(Optional.empty());
        when(repo.findByEmail("u1@example.com")).thenReturn(Optional.empty());
        when(encoder.encode("pwd")).thenReturn("hashpwd");

        when(repo.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(5L);
            return u;
        });

        UserResponse res = userService.register(registerReq);

        assertEquals(5L, res.getId());
        assertEquals("u1", res.getUsername());
        assertEquals("u1@example.com", res.getEmail());
        assertEquals("USER", res.getRole());
    }

    @Test
    void register_usernameExists_throws() {
        when(repo.findByUsername("u1")).thenReturn(Optional.of(new User()));
        BusinessException ex = assertThrows(BusinessException.class, () -> userService.register(registerReq));
        assertTrue(ex.getMessage().contains("Username already exists"));
    }

    @Test
    void register_emailExists_throws() {
        when(repo.findByUsername("u1")).thenReturn(Optional.empty());
        when(repo.findByEmail("u1@example.com")).thenReturn(Optional.of(new User()));
        BusinessException ex = assertThrows(BusinessException.class, () -> userService.register(registerReq));
        assertTrue(ex.getMessage().contains("Email already exists"));
    }

    @Test
    void login_success_and_failures() {
        User u = new User();
        u.setId(10L);
        u.setEmail("e@x.com");
        u.setPassword("hash");
        u.setActive(true);
        u.setRole(Role.USER);

        LoginRequest req = new LoginRequest();
        req.setCredential("u1");
        req.setPassword("pwd");

        when(repo.findByUsernameOrEmail("u1","u1")).thenReturn(Optional.of(u));
        when(encoder.matches("pwd","hash")).thenReturn(true);
        when(jwtUtil.generateToken(10L, "e@x.com", "USER")).thenReturn("tok");

        AuthResponse ar = userService.login(req);
        assertEquals("tok", ar.getToken());

        // invalid credential
        when(repo.findByUsernameOrEmail("x","x")).thenReturn(Optional.empty());
        LoginRequest bad = new LoginRequest(); bad.setCredential("x"); bad.setPassword("p");
        assertThrows(BusinessException.class, () -> userService.login(bad));

        // inactive user
        u.setActive(false);
        when(repo.findByUsernameOrEmail("u1","u1")).thenReturn(Optional.of(u));
        assertThrows(BusinessException.class, () -> userService.login(req));

        // wrong password
        u.setActive(true);
        when(encoder.matches("pwd","hash")).thenReturn(false);
        assertThrows(BusinessException.class, () -> userService.login(req));
    }

    @Test
    void activate_deactivate_and_list() {
        User u = new User();
        u.setId(8L);
        u.setActive(false);
        u.setRole(Role.USER);

        when(repo.findById(8L)).thenReturn(Optional.of(u));
        userService.activate(8L);
        assertTrue(u.isActive());

        userService.deactivate(8L);
        assertFalse(u.isActive());

        when(repo.findAll()).thenReturn(List.of(u));
        var list = userService.listUsers();
        assertEquals(1, list.size());
    }

    @Test
    void activate_notFound_throws() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> userService.activate(99L));
    }
}
