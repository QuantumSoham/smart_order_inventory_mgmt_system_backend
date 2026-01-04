package com.chubb.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chubb.user.dto.request.*;
import com.chubb.user.dto.response.*;
import com.chubb.user.service.UserService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // PUBLIC
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest req) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest req) {

        return ResponseEntity.ok(service.login(req));
    }

    // ADMIN
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.ok(
                Map.of("message", "User activated successfully"));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.ok(
                Map.of("message", "User deactivated successfully"));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.ok(service.listUsers());
    }
}

//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService service;
//
//    // PUBLIC
//    @PostMapping("/register")
//    public UserResponse register(@RequestBody RegisterRequest req) {
//        return service.register(req);
//    }
//
//    @PostMapping("/login")
//    public AuthResponse login(@RequestBody LoginRequest req) {
//        return service.login(req);
//    }
//
//    // ADMIN
//    @PutMapping("/{id}/activate")
//    public void activate(@PathVariable Long id) {
//        service.activate(id);
//    }
//
//    @PutMapping("/{id}/deactivate")
//    public void deactivate(@PathVariable Long id) {
//        service.deactivate(id);
//    }
//
//    @GetMapping
//    public List<UserResponse> listUsers() {
//        return service.listUsers();
//    }
//}
