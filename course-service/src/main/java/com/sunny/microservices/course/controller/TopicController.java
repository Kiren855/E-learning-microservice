package com.sunny.microservices.course.controller;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.response.TopicResponse;
import com.sunny.microservices.course.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("public")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TopicController {
    TopicService topicService;

    @GetMapping("/topics")
    ResponseEntity<ApiResponse<List<TopicResponse>>> getTopics() {
        ApiResponse<List<TopicResponse>> response = ApiResponse.<List<TopicResponse>>builder()
                .message("lấy danh sách topic thành công")
                .result(topicService.getTopics()).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
