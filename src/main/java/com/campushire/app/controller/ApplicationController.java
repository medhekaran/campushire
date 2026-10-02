package com.campushire.app.controller;

import com.campushire.app.dto.ApplicationResponse;
import com.campushire.app.entity.Application;
import com.campushire.app.entity.StudentProfile;
import com.campushire.app.service.ApplicationService;
import com.campushire.app.service.StudentProfileService;
import com.campushire.app.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private StudentProfileService studentProfileService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> apply(@RequestParam Long driveId, HttpSession session) {
        Long userId = SessionUtil.requireRole(session, "STUDENT");
        StudentProfile student = studentProfileService.getByUserId(userId);
        Application saved = applicationService.apply(student.getId(), driveId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApplicationResponse.from(saved));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(HttpSession session) {
        Long userId = SessionUtil.requireRole(session, "STUDENT");
        StudentProfile student = studentProfileService.getByUserId(userId);
        return ResponseEntity.ok(applicationService.getByStudent(student.getId()).stream()
                .map(ApplicationResponse::from).toList());
    }

    @GetMapping("/drive/{driveId}")
    public ResponseEntity<List<ApplicationResponse>> getByDrive(
            @PathVariable Long driveId, HttpSession session) {
        Long userId = SessionUtil.requireRole(session, "RECRUITER", "ADMIN");
        return ResponseEntity.ok(
                applicationService.getByDrive(driveId, userId, SessionUtil.getRole(session)).stream()
                        .map(ApplicationResponse::from).toList());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id, @RequestParam String status, HttpSession session) {
        Long userId = SessionUtil.requireRole(session, "RECRUITER", "ADMIN");
        Application updated = applicationService.updateStatus(id, status, userId, SessionUtil.getRole(session));
        return ResponseEntity.ok(ApplicationResponse.from(updated));
    }
}