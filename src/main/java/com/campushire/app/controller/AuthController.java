package com.campushire.app.controller;

import com.campushire.app.config.LoginRateLimiter;
import com.campushire.app.dto.LoginRequest;
import com.campushire.app.dto.RegisterRequest;
import com.campushire.app.dto.UserResponse;
import com.campushire.app.entity.User;
import com.campushire.app.exception.BusinessRuleException;
import com.campushire.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginRateLimiter rateLimiter;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User saved = userService.register(request);
        UserResponse response = new UserResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        if (rateLimiter.isBlocked(request.getEmail())) {
            throw new BusinessRuleException("Too many failed login attempts. Try again in a minute.");
        }

        try {
            User user = userService.login(request.getEmail(), request.getPassword());
            rateLimiter.clearAttempts(request.getEmail());

            // Store identity in the session so later requests know who's logged in
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole());

            UserResponse response = new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException ex) {
            rateLimiter.recordFailedAttempt(request.getEmail());
            throw ex;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        return ResponseEntity.ok(Map.of("userId", userId, "role", session.getAttribute("role")));
    }
}