package com.campushire.app.serviceimpl;

import com.campushire.app.entity.Application;
import com.campushire.app.entity.CompanyDrive;
import com.campushire.app.entity.StudentProfile;
import com.campushire.app.exception.BusinessRuleException;
import com.campushire.app.exception.DuplicateResourceException;
import com.campushire.app.exception.ForbiddenException;
import com.campushire.app.exception.ResourceNotFoundException;
import com.campushire.app.repository.ApplicationRepository;
import com.campushire.app.repository.CompanyDriveRepository;
import com.campushire.app.repository.StudentProfileRepository;
import com.campushire.app.service.ApplicationService;
import org.springframework.stereotype.Service;
import com.campushire.app.exception.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final List<String> VALID_STATUSES =
            List.of("APPLIED", "SHORTLISTED", "SELECTED", "REJECTED");

    private final ApplicationRepository applicationRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CompanyDriveRepository companyDriveRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  StudentProfileRepository studentProfileRepository,
                                  CompanyDriveRepository companyDriveRepository) {
        this.applicationRepository = applicationRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.companyDriveRepository = companyDriveRepository;
    }

    @Override
    public Application apply(Long studentProfileId, Long driveId) {
        if (applicationRepository.findByStudentIdAndDriveId(studentProfileId, driveId).isPresent()) {
            throw new DuplicateResourceException("You have already applied to this drive");
        }

        StudentProfile student = studentProfileRepository.findById(studentProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
        CompanyDrive drive = companyDriveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found"));

        if (!"APPROVED".equals(drive.getStatus())) {
            throw new BusinessRuleException("This drive is not open for applications");
        }

        if (student.getCgpa().compareTo(drive.getMinCgpa()) < 0
                || student.getBacklogs() > drive.getAllowedBacklogs()) {
            throw new BusinessRuleException("You do not meet the eligibility criteria for this drive");
        }

        Application application = new Application();
        application.setStudent(student);
        application.setDrive(drive);
        application.setStatus("APPLIED");
        application.setAppliedOn(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    @Override
    public Application updateStatus(Long applicationId, String status, Long requesterId, String requesterRole) {
        if (!VALID_STATUSES.contains(status)) {
            throw new BusinessRuleException("Status must be APPLIED, SHORTLISTED, SELECTED or REJECTED");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        checkDriveAccess(application.getDrive(), requesterId, requesterRole);

        application.setStatus(status);
        return applicationRepository.save(application);
    }

    @Override
    public List<Application> getByStudent(Long studentProfileId) {
        return applicationRepository.findByStudentId(studentProfileId);
    }

    @Override
    public List<Application> getByDrive(Long driveId, Long requesterId, String requesterRole) {
        CompanyDrive drive = companyDriveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found"));

        checkDriveAccess(drive, requesterId, requesterRole);

        return applicationRepository.findByDriveId(driveId);
    }

    // Admins can manage any drive. A recruiter can manage only their own drives.
    private void checkDriveAccess(CompanyDrive drive, Long requesterId, String requesterRole) {
        boolean isAdmin = "ADMIN".equals(requesterRole);
        boolean isOwner = drive.getRecruiter().getId().equals(requesterId);

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You can only manage applications for your own drives");
        }
    }
}