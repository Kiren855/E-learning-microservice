package com.sunny.microservices.learning.service;

import com.sunny.microservices.basedomain.course.dto.response.CourseLearningResponse;
import com.sunny.microservices.learning.client.CourseClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LearningService {
        CourseClient courseClient;

        @Cacheable(value = "courses", key = "#courseId")
        public CourseLearningResponse getDetailCourseForLearning(String courseId) {
            return courseClient.getCourseDetail(courseId);
        }
}
