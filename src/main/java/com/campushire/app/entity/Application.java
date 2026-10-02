package com.campushire.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @ManyToOne
    @JoinColumn(name = "drive_id", nullable = false)
    private CompanyDrive drive;

    private String status = "APPLIED";

    @Column(name = "applied_on")
    private LocalDateTime appliedOn;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentProfile getStudent() { return student; }
    public void setStudent(StudentProfile student) { this.student = student; }

    public CompanyDrive getDrive() { return drive; }
    public void setDrive(CompanyDrive drive) { this.drive = drive; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getAppliedOn() { return appliedOn; }
    public void setAppliedOn(LocalDateTime appliedOn) { this.appliedOn = appliedOn; }
}