package com.campushire.app.dto;

import com.campushire.app.entity.Application;
import com.campushire.app.entity.CompanyDrive;
import com.campushire.app.entity.StudentProfile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long driveId,
        String companyName,
        String jobTitle,
        BigDecimal packageLpa,
        String studentName,
        String branch,
        BigDecimal cgpa,
        String status,
        LocalDateTime appliedOn
) {
    public static ApplicationResponse from(Application application) {
        CompanyDrive drive = application.getDrive();
        StudentProfile student = application.getStudent();
        return new ApplicationResponse(
                application.getId(),
                drive.getId(),
                drive.getCompanyName(),
                drive.getJobTitle(),
                drive.getPackageLpa(),
                student.getUser().getName(),
                student.getBranch(),
                student.getCgpa(),
                application.getStatus(),
                application.getAppliedOn());
    }
}