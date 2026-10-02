package com.campushire.app.service;

import com.campushire.app.entity.Application;

import java.util.List;

public interface ApplicationService {
    Application apply(Long studentProfileId, Long driveId);
    Application updateStatus(Long applicationId, String status, Long requesterId, String requesterRole);
    List<Application> getByStudent(Long studentProfileId);
    List<Application> getByDrive(Long driveId, Long requesterId, String requesterRole);
}