package com.chubb.user.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.chubb.user.dto.request.*;
import com.chubb.user.dto.response.*;
import com.chubb.user.entity.*;
import com.chubb.user.exception.BusinessException;
import com.chubb.user.repository.UserRepository;
import com.chubb.user.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    // REGISTER
    public UserResponse register(RegisterRequest req) {

        if (repo.findByUsername(req.getUsername()).isPresent())
            throw new BusinessException("Username already exists");

        if (repo.findByEmail(req.getEmail()).isPresent())
            throw new BusinessException("Email already exists");

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(Role.valueOf(req.getRole()));
        user.setActive(true);

        repo.save(user);

        return toResponse(user);
    }

    // LOGIN → JWT GENERATED HERE
    public AuthResponse login(LoginRequest req) {

        User user = repo.findByUsernameOrEmail(
                        req.getCredential(),
                        req.getCredential()
                )
                .orElseThrow(() ->
                        new BusinessException("Invalid credentials")
                );

        if (!user.isActive())
            throw new BusinessException("User account is deactivated");

        if (!encoder.matches(req.getPassword(), user.getPassword()))
            throw new BusinessException("Invalid credentials");

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token);
    }

//    public AuthResponse login(LoginRequest req) {
//
//        User user = repo.findByUsernameOrEmail(req.getCredential(),req.getCredential())
//                .orElseThrow(() -> new BusinessException("Invalid credentials"));
//
//        if (!user.isActive())
//            throw new BusinessException("User is deactivated");
//
//        if (!encoder.matches(req.getPassword(), user.getPassword()))
//            throw new BusinessException("Invalid credentials");
//
//        String token =
//                jwtUtil.generateToken(
//                        user.getId(),
//                        user.getEmail(),
//                        user.getRole().name()
//                );
//
//        return new AuthResponse(token);
//    }

    // ADMIN
    public void activate(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
        user.setActive(true);
    }

    public void deactivate(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
        user.setActive(false);
    }

    public List<UserResponse> listUsers() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole().name(),
                u.isActive()
        );
    }
}
