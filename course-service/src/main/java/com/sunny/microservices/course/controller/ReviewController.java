package com.sunny.microservices.course.controller;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.ReviewRequest;
import com.sunny.microservices.course.dto.response.IdResponse;
import com.sunny.microservices.course.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReviewController {
    ReviewService reviewService;

    @PostMapping("/{courseId}")
    public ResponseEntity<ApiResponse<IdResponse>> createReview(@PathVariable String courseId, @RequestBody ReviewRequest request) {
        ApiResponse<IdResponse> response = ApiResponse.<IdResponse>builder()
                .message("tạo bài đánh giá thành công")
                .result(reviewService.createReview(courseId, request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<String>> updateReview(@PathVariable String reviewId,@RequestBody ReviewRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(reviewService.updateReview(reviewId, request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
