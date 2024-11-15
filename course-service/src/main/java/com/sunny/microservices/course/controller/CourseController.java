package com.sunny.microservices.course.controller;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.CourseRequest;
import com.sunny.microservices.course.dto.response.CoursePreviewResponse;
import com.sunny.microservices.course.dto.response.CourseResponse;
import com.sunny.microservices.course.service.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseController {

    CourseService courseService;

    @GetMapping("/preview/{courseId}")
    public ResponseEntity<?> getCourse(@PathVariable String courseId) {
        ApiResponse<CoursePreviewResponse> response = ApiResponse.<CoursePreviewResponse>builder()
                .message("lấy thông tin khoá học thành công")
                .result(courseService.getCoursePreview(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping()
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> createCourse(@ModelAttribute CourseRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseService.createCourse(request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateCourse(@PathVariable String courseId, @RequestBody CourseRequest request) {
        courseService.updateCourse(courseId, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);

        return ResponseEntity.noContent().build();
    }
}
