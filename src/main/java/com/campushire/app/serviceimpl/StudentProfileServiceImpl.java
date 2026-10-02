package com.campushire.app.serviceimpl;

import com.campushire.app.dto.StudentProfileRequest;
import com.campushire.app.entity.StudentProfile;
import com.campushire.app.entity.User;
import com.campushire.app.exception.ResourceNotFoundException;
import com.campushire.app.repository.StudentProfileRepository;
import com.campushire.app.repository.UserRepository;
import com.campushire.app.service.StudentProfileService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository, UserRepository userRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<StudentProfile> findByUserId(Long userId) {
        return studentProfileRepository.findByUserId(userId);
    }

    @Override
    public StudentProfile getByUserId(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Please complete your profile first"));
    }

    @Override
    public StudentProfile saveOrUpdate(Long userId, StudentProfileRequest request) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                    StudentProfile created = new StudentProfile();
                    created.setUser(user);
                    return created;
                });

        profile.setBranch(request.branch());
        profile.setCgpa(request.cgpa());
        profile.setBacklogs(request.backlogs());

        return studentProfileRepository.save(profile);
    }
}