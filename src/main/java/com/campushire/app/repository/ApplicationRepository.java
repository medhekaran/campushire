package com.campushire.app.repository;

import com.campushire.app.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentId(Long studentId);

    List<Application> findByDriveId(Long driveId);

    Optional<Application> findByStudentIdAndDriveId(Long studentId, Long driveId);
}