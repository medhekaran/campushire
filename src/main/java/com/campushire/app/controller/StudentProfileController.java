package com.campushire.app.controller;

import com.campushire.app.dto.StudentProfileRequest;
import com.campushire.app.dto.StudentProfileResponse;
import com.campushire.app.service.StudentProfileService;
import com.campushire.app.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentProfileController {

    @Autowired
    private StudentProfileService studentProfileService;

    @GetMapping("/me")
    public ResponseEntity<StudentProfileResponse> getMyProfile(HttpSession session) {
        Long userId = SessionUtil.requireRole(session, "STUDENT");
        return studentProfileService.findByUserId(userId)
                .map(profile -> ResponseEntity.ok(StudentProfileResponse.from(profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<StudentProfileResponse> saveMyProfile(
            @Valid @RequestBody StudentProfileRequest request, HttpSession session) {
        Long userId = SessionUtil.requireRole(session, "STUDENT");
        return ResponseEntity.ok(
                StudentProfileResponse.from(studentProfileService.saveOrUpdate(userId, request)));
    }
}