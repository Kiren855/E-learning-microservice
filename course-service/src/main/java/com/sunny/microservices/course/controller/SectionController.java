package com.sunny.microservices.course.controller;


import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.SectionRequest;
import com.sunny.microservices.course.dto.response.IdResponse;
import com.sunny.microservices.course.service.SectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sections")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SectionController {
    SectionService sectionService;
    @PostMapping("/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<IdResponse>> createSection(@PathVariable String courseId, @RequestBody SectionRequest request) {
        ApiResponse<IdResponse> response = ApiResponse.<IdResponse>builder()
                .message("tạo phần học thành công")
                .result(sectionService.createSection(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateSection(@PathVariable String sectionId, SectionRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(sectionService.updateSection(sectionId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteSection(@RequestParam String courseId, @RequestParam String sectionId) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(sectionService.deleteSection(courseId, sectionId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
