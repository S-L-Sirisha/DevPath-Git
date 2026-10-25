package com.devpath.controller;

import com.devpath.dto.AuthRequest;
import com.devpath.dto.AuthResponse;
import com.devpath.dto.RegisterRequest;
import com.devpath.entity.User;
import com.devpath.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentAuthenticatedUser());
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody Map<String, String> body) {
        User current = authService.getCurrentAuthenticatedUser();
        User updated = authService.updateProfile(
                current,
                body.get("name"),
                body.get("targetRole"),
                body.get("college")
        );
        return ResponseEntity.ok(updated);
    }
}
