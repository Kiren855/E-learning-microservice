package com.sunny.microservices.course.controller;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.response.CourseDetailResponse;
import com.sunny.microservices.course.dto.response.CoursePreviewResponse;
import com.sunny.microservices.course.service.CourseService;
import com.sunny.microservices.course.service.LessonService;
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

@RestController
@RequestMapping("internal/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseClientController {
    CourseService courseService;
    LessonService lessonService;

    @GetMapping("/detail/{courseId}")
    public ResponseEntity<?> getCourse(@PathVariable String courseId) {
        ApiResponse<CourseDetailResponse> response = ApiResponse.<CourseDetailResponse>builder()
                .message("lấy thông tin khoá học thành công")
                .result(courseService.getCourseDetail(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @GetMapping("/learn/{lessonId}")
//    public ResponseEntity<?> getLesson(@PathVariable String lessonId) {
//        ApiResponse<CourseDetailResponse> response = ApiResponse.<CourseDetailResponse>builder()
//                .message("lấy thông tin bài hc thành công")
//                .result(lessonService.getLessonDetail(lessonId)).build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
}
