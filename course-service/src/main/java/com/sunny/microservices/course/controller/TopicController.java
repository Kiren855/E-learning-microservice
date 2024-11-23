package com.sunny.microservices.course.controller;


import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.TopicRequest;
import com.sunny.microservices.course.dto.response.TopicResponse;
import com.sunny.microservices.course.service.TopicService;
import jakarta.validation.Valid;
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
@RequestMapping("topics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TopicController {
    TopicService topicService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTopic(@RequestBody @Valid TopicRequest request) {
        ApiResponse<TopicResponse> response = ApiResponse.<TopicResponse>builder()
                .message(topicService.createTopic(request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<?> getTopics() {
        List<TopicResponse> topicResponses = topicService.getTopics();
        ApiResponse<List<TopicResponse>> response = ApiResponse.<List<TopicResponse>>builder()
                .code(1000)
                .message("Topics fetched successfully")
                .result(topicResponses)
                .build();

        return ResponseEntity.ok(response);
    }
}
