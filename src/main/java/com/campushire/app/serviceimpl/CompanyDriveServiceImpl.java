package com.campushire.app.serviceimpl;

import com.campushire.app.dto.DriveRequest;
import com.campushire.app.entity.CompanyDrive;
import com.campushire.app.entity.StudentProfile;
import com.campushire.app.entity.User;
import com.campushire.app.exception.BusinessRuleException;
import com.campushire.app.exception.ResourceNotFoundException;
import com.campushire.app.repository.CompanyDriveRepository;
import com.campushire.app.repository.UserRepository;
import com.campushire.app.service.CompanyDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyDriveServiceImpl implements CompanyDriveService {

    @Autowired
    private CompanyDriveRepository companyDriveRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CompanyDrive postDrive(Long recruiterId, DriveRequest request) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        CompanyDrive drive = new CompanyDrive();
        drive.setRecruiter(recruiter);
        drive.setCompanyName(request.companyName());
        drive.setJobTitle(request.jobTitle());
        drive.setPackageLpa(request.packageLpa());
        drive.setMinCgpa(request.minCgpa());
        drive.setAllowedBacklogs(request.allowedBacklogs());
        drive.setStatus("PENDING");
        drive.setCreatedAt(LocalDateTime.now());

        return companyDriveRepository.save(drive);
    }

    @Override
    public CompanyDrive approveDrive(Long driveId) {
        CompanyDrive drive = companyDriveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found"));
        drive.setStatus("APPROVED");
        return companyDriveRepository.save(drive);
    }

    @Override
    public List<CompanyDrive> getEligibleDrivesForStudent(StudentProfile student) {
        return companyDriveRepository
                .findByStatusAndMinCgpaLessThanEqualAndAllowedBacklogsGreaterThanEqual(
                        "APPROVED", student.getCgpa(), student.getBacklogs());
    }

    @Override
    public List<CompanyDrive> getDrivesByRecruiter(Long recruiterId) {
        return companyDriveRepository.findByRecruiterId(recruiterId);
    }

    @Override
    public List<CompanyDrive> getDrivesByStatus(String status) {
        if (!List.of("PENDING", "APPROVED", "REJECTED").contains(status)) {
            throw new BusinessRuleException("Status must be PENDING, APPROVED or REJECTED");
        }
        return companyDriveRepository.findByStatus(status);
    }
}