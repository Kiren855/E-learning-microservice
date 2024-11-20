package com.sunny.microservices.learning.client;


import com.sunny.microservices.basedomain.course.dto.response.CourseDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "course", url = "${course-service.url}")
public interface CourseClient {
    @GetMapping("/internal/courses/detail/{courseId}")
    CourseDetailResponse getCourseDetail(@PathVariable String courseId);

    @GetMapping("/internal/courses/learn/{lessonId}")
    ResponseEntity<?> getLessonDetail(@PathVariable String lessonId);
}
