package com.campushire.app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "company_drive")
public class CompanyDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiter;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "package_lpa", nullable = false)
    private BigDecimal packageLpa;

    @Column(name = "min_cgpa", nullable = false)
    private BigDecimal minCgpa;

    @Column(name = "allowed_backlogs")
    private Integer allowedBacklogs = 0;

    private String status = "PENDING";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getRecruiter() { return recruiter; }
    public void setRecruiter(User recruiter) { this.recruiter = recruiter; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public BigDecimal getPackageLpa() { return packageLpa; }
    public void setPackageLpa(BigDecimal packageLpa) { this.packageLpa = packageLpa; }

    public BigDecimal getMinCgpa() { return minCgpa; }
    public void setMinCgpa(BigDecimal minCgpa) { this.minCgpa = minCgpa; }

    public Integer getAllowedBacklogs() { return allowedBacklogs; }
    public void setAllowedBacklogs(Integer allowedBacklogs) { this.allowedBacklogs = allowedBacklogs; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}