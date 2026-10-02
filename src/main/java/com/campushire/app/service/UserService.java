package com.campushire.app.service;

import com.campushire.app.dto.RegisterRequest;
import com.campushire.app.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    User login(String email, String rawPassword);
}