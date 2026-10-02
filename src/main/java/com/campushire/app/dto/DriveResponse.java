package com.campushire.app.dto;

import com.campushire.app.entity.CompanyDrive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DriveResponse(
        Long id,
        String companyName,
        String jobTitle,
        BigDecimal packageLpa,
        BigDecimal minCgpa,
        Integer allowedBacklogs,
        String status,
        LocalDateTime createdAt
) {
    public static DriveResponse from(CompanyDrive drive) {
        return new DriveResponse(
                drive.getId(),
                drive.getCompanyName(),
                drive.getJobTitle(),
                drive.getPackageLpa(),
                drive.getMinCgpa(),
                drive.getAllowedBacklogs(),
                drive.getStatus(),
                drive.getCreatedAt());
    }
}