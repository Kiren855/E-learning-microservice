package com.sunny.microservices.learning.service;

import com.sunny.microservices.learning.dto.response.CourseEnrolledResponse;
import com.sunny.microservices.learning.entity.Enrollment;
import com.sunny.microservices.learning.repository.EnrollmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LearningService {
    EnrollmentRepository enrollmentRepository;

    public List<CourseEnrolledResponse> getCourseEnrolleds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        return enrollments.stream().map(enrolled-> CourseEnrolledResponse.builder()
                .courseId(enrolled.getCourseId())
                .courseName(enrolled.getCourseName())
                .instructorName(enrolled.getInstructorName())
                .image(enrolled.getImage())
                .completionRate(enrolled.getProgress().getCompletionRate()).build())
                .toList();
    }
}
