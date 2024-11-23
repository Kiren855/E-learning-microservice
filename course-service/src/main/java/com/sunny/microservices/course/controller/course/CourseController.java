package com.sunny.microservices.course.controller.course;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.course.CourseCreateRequest;
import com.sunny.microservices.course.dto.request.course.CourseRequest;
import com.sunny.microservices.course.dto.response.course.CoursePreviewResponse;
import com.sunny.microservices.course.dto.response.course.CourseResponse;
import com.sunny.microservices.course.service.course.CourseCreateService;
import com.sunny.microservices.course.service.course.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseController {

    CourseService courseService;
    CourseCreateService courseCreateService;
    @GetMapping("/preview/{courseId}")
    public ResponseEntity<?> getCourse(@PathVariable String courseId) {
        ApiResponse<CoursePreviewResponse> response = ApiResponse.<CoursePreviewResponse>builder()
                .message("lấy thông tin khoá học thành công")
                .result(courseService.getCoursePreview(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping()
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> createCourse(@RequestBody CourseCreateRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseCreateService.createCourse(request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> getCourses() {
        ApiResponse<List<CourseResponse>> response = ApiResponse.<List<CourseResponse>>builder()
                .message("Lấy danh sách khoá học thành công")
                .result(courseService.getCourses()).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
