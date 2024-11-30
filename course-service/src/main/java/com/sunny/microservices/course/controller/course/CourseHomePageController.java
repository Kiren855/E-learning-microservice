package com.sunny.microservices.course.controller.course;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.response.course.CourseHomePageResponse;
import com.sunny.microservices.course.service.course.CourseHomePageService;
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
@RequestMapping("courses-home")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseHomePageController {

    CourseHomePageService courseHomePageService;

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<List<CourseHomePageResponse>>> getCourses() {
        ApiResponse<List<CourseHomePageResponse>> response = ApiResponse.<List<CourseHomePageResponse>>builder()
                .message("lấy danh sách khoá học thành công")
                .result(courseHomePageService.getCourses()).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/recommend/{mainTopic}")
    public ResponseEntity<ApiResponse<List<CourseHomePageResponse>>> getCoursesByTopic(@PathVariable String mainTopic) {
        ApiResponse<List<CourseHomePageResponse>> response = ApiResponse.<List<CourseHomePageResponse>>builder()
                .message("lấy danh sách khoá học thành công")
                .result(courseHomePageService.getCourseByMainTopic(mainTopic)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
