package com.campushire.app.repository;

import com.campushire.app.entity.CompanyDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.List;

public interface CompanyDriveRepository extends JpaRepository<CompanyDrive, Long> {

    List<CompanyDrive> findByStatus(String status);

    List<CompanyDrive> findByRecruiterId(Long recruiterId);

    List<CompanyDrive> findByStatusAndMinCgpaLessThanEqualAndAllowedBacklogsGreaterThanEqual(
        String status, BigDecimal cgpa, Integer backlogs
    );
}