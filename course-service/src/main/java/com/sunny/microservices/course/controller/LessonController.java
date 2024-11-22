package com.sunny.microservices.course.controller;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.lesson.DocLessonRequest;
import com.sunny.microservices.course.dto.request.lesson.ExamRequest;
import com.sunny.microservices.course.dto.request.lesson.LessonRequest;
import com.sunny.microservices.course.dto.request.lesson.VideoLessonRequest;
import com.sunny.microservices.course.service.lesson.LessonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("section/lessons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LessonController {
    LessonService lessonService;

    @PostMapping("/video/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> createVideoLesson(@PathVariable String sectionId,
                                               @ModelAttribute VideoLessonRequest request) throws IOException {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.createVideoLesson(sectionId, request))
               .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/doc/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> createDocLesson(@PathVariable String sectionId,
                                             @ModelAttribute DocLessonRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.createDocLesson(sectionId, request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/exam/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> createExamLesson(@PathVariable String sectionId, @RequestBody ExamRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.createExam(sectionId, request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateLesson(@PathVariable String lessonId, @RequestBody LessonRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.updateLesson(lessonId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteLesson(@RequestParam String sectionId, @RequestParam String lessonId) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.deleteLesson(sectionId, lessonId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
