package com.campushire.app.controller;

import com.campushire.app.dto.DriveRequest;
import com.campushire.app.dto.DriveResponse;
import com.campushire.app.entity.StudentProfile;
import com.campushire.app.service.CompanyDriveService;
import com.campushire.app.service.StudentProfileService;
import com.campushire.app.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drives")
public class CompanyDriveController {

    @Autowired
    private CompanyDriveService companyDriveService;

    @Autowired
    private StudentProfileService studentProfileService;

    @PostMapping
    public ResponseEntity<DriveResponse> postDrive(
            @Valid @RequestBody DriveRequest request, HttpSession session) {
        Long recruiterId = SessionUtil.requireRole(session, "RECRUITER");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DriveResponse.from(companyDriveService.postDrive(recruiterId, request)));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<DriveResponse>> getMyDrives(HttpSession session) {
        Long recruiterId = SessionUtil.requireRole(session, "RECRUITER");
        return ResponseEntity.ok(companyDriveService.getDrivesByRecruiter(recruiterId).stream()
                .map(DriveResponse::from).toList());
    }

    @GetMapping
    public ResponseEntity<List<DriveResponse>> getByStatus(
            @RequestParam String status, HttpSession session) {
        SessionUtil.requireRole(session, "ADMIN");
        return ResponseEntity.ok(companyDriveService.getDrivesByStatus(status).stream()
                .map(DriveResponse::from).toList());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<DriveResponse> approveDrive(@PathVariable Long id, HttpSession session) {
        SessionUtil.requireRole(session, "ADMIN");
        return ResponseEntity.ok(DriveResponse.from(companyDriveService.approveDrive(id)));
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<DriveResponse>> getEligibleDrives(HttpSession session) {
        Long userId = SessionUtil.requireRole(session, "STUDENT");
        StudentProfile student = studentProfileService.getByUserId(userId);
        return ResponseEntity.ok(companyDriveService.getEligibleDrivesForStudent(student).stream()
                .map(DriveResponse::from).toList());
    }
}