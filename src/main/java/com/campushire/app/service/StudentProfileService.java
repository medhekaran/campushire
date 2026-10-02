package com.campushire.app.service;

import com.campushire.app.dto.StudentProfileRequest;
import com.campushire.app.entity.StudentProfile;

import java.util.Optional;

public interface StudentProfileService {
    Optional<StudentProfile> findByUserId(Long userId);
    StudentProfile getByUserId(Long userId);
    StudentProfile saveOrUpdate(Long userId, StudentProfileRequest request);
}