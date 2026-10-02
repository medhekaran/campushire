package com.campushire.app.dto;

import com.campushire.app.entity.StudentProfile;

import java.math.BigDecimal;

public record StudentProfileResponse(
        Long id,
        String name,
        String email,
        String branch,
        BigDecimal cgpa,
        Integer backlogs
) {
    public static StudentProfileResponse from(StudentProfile profile) {
        return new StudentProfileResponse(
                profile.getId(),
                profile.getUser().getName(),
                profile.getUser().getEmail(),
                profile.getBranch(),
                profile.getCgpa(),
                profile.getBacklogs());
    }
}