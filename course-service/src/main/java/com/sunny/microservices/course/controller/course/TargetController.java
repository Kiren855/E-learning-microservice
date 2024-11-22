package com.sunny.microservices.course.controller.course;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.course.CourseTargetRequest;
import com.sunny.microservices.course.service.course.TargetService;
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
public class TargetController {
    TargetService targetService;

    @PostMapping("/target/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> createTarget(@PathVariable String courseId, CourseTargetRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(targetService.createTarget(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/target/{courseId}")
    public ResponseEntity<?> getTarget(@PathVariable String courseId) {
        ApiResponse<List<String>> response = ApiResponse.<List<String>>builder()
                .message("lấy danh sách mục tiêu thành công")
                .result(targetService.getTarget(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
