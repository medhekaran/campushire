package com.campushire.app.service;

import com.campushire.app.dto.DriveRequest;
import com.campushire.app.entity.CompanyDrive;
import com.campushire.app.entity.StudentProfile;

import java.util.List;

public interface CompanyDriveService {
    CompanyDrive postDrive(Long recruiterId, DriveRequest request);
    CompanyDrive approveDrive(Long driveId);
    List<CompanyDrive> getEligibleDrivesForStudent(StudentProfile student);
    List<CompanyDrive> getDrivesByRecruiter(Long recruiterId);
    List<CompanyDrive> getDrivesByStatus(String status);
}