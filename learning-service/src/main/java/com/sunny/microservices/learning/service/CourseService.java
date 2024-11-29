package com.sunny.microservices.learning.service;

import com.sunny.microservices.basedomain.course.dto.response.CourseLearningResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService {
    @CachePut(value = "courses", key = "#event.id")
    public CourseLearningResponse updateCourseCache(CourseLearningResponse event) {
        return event;
    }
}
