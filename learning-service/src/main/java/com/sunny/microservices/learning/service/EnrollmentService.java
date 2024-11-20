package com.sunny.microservices.learning.service;

import com.sunny.microservices.learning.client.CourseClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentService {
    CourseClient courseClient;

    public ResponseEntity<?> getCourse(String courseId) {
        return courseClient.getCourseDetail(courseId);
    }
}
