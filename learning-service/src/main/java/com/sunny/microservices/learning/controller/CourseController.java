package com.sunny.microservices.learning.controller;

import com.sunny.microservices.basedomain.course.dto.response.CourseLearningResponse;
import com.sunny.microservices.learning.dto.ApiResponse;
import com.sunny.microservices.learning.dto.response.CourseEnrolledResponse;
import com.sunny.microservices.learning.service.CourseService;
import com.sunny.microservices.learning.service.LearningService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseController {

    CourseService courseService;
    LearningService learningService;
    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseLearningResponse>> getCourse(@PathVariable String courseId){
        ApiResponse<CourseLearningResponse> response = ApiResponse.<CourseLearningResponse>builder()
                .message("lấy thông tin khoá học thành công")
                .result(courseService.getDetailCourseForLearning(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<CourseEnrolledResponse>>> getCourseEnrolled() {
            ApiResponse<List<CourseEnrolledResponse>> response = ApiResponse.<List<CourseEnrolledResponse>>builder()
                    .message("lấy khoá học đã đăng ký thành công")
                    .result(learningService.getCourseEnrolleds()).build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
