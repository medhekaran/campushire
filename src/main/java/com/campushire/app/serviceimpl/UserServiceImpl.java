package com.campushire.app.serviceimpl;

import com.campushire.app.dto.RegisterRequest;
import com.campushire.app.entity.User;
import com.campushire.app.exception.BusinessRuleException;
import com.campushire.app.exception.DuplicateResourceException;
import com.campushire.app.repository.UserRepository;
import com.campushire.app.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.campushire.app.exception.*;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    // ADMIN is deliberately missing: admins are never created through public registration
    private static final List<String> PUBLIC_ROLES = List.of("STUDENT", "RECRUITER");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(RegisterRequest request) {
        String role = request.getRole() != null ? request.getRole().trim().toUpperCase() : null;
        if (role == null || !PUBLIC_ROLES.contains(role)) {
            throw new BusinessRuleException("Role must be STUDENT or RECRUITER");
        }

        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalStateException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalStateException("Invalid email or password");
        }
        return user;
    }
}