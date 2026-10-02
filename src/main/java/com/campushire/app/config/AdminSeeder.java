package com.campushire.app.config;

import com.campushire.app.entity.User;
import com.campushire.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    @Value("${campushire.admin.email:}")
    private String adminEmail;

    @Value("${campushire.admin.password:}")
    private String adminPassword;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.info("Admin credentials not configured. Skipping admin account creation.");
            return;
        }

        String email = adminEmail.trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            log.info("Admin account already exists.");
            return;
        }

        User admin = new User();
        admin.setName("Placement Officer");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole("ADMIN");
        userRepository.save(admin);

        log.info("Admin account created for {}", email);
    }
}