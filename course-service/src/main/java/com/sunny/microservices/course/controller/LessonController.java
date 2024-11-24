package com.sunny.microservices.course.controller;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.lesson.*;
import com.sunny.microservices.course.dto.response.lesson.ArticleResponse;
import com.sunny.microservices.course.dto.response.lesson.ExamResponse;
import com.sunny.microservices.course.dto.response.lesson.VideoResponse;
import com.sunny.microservices.course.service.lesson.LessonService;
import com.sunny.microservices.course.service.lesson.SubLessonService;
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
    SubLessonService subLessonService;
    /////////////////////// VIDEO //////////////////////////////////////////
    @GetMapping("/video/{typeId}")
    public ResponseEntity<ApiResponse<VideoResponse>> getVideo(@PathVariable String typeId) {
        ApiResponse<VideoResponse> response = ApiResponse.<VideoResponse>builder()
                .message("Lấy video thành công")
                .result(subLessonService.getVideoLesson(typeId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/video/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> createVideoLesson(@PathVariable String sectionId,
                                               @ModelAttribute VideoLessonRequest request) throws IOException {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.createVideoLesson(sectionId, request))
               .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //////////////// DOCUMENT /////////////////////////////////////////////
    @PostMapping("/doc/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> createDocLesson(@PathVariable String sectionId,
                                             @ModelAttribute DocLessonRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.createDocLesson(sectionId, request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    ///////////// EXAM //////////////////////////////////////////////////////
    @GetMapping("/exam/{typeId}")
    public ResponseEntity<ApiResponse<ExamResponse>> getExam(@PathVariable String typeId) {
        ApiResponse<ExamResponse> response = ApiResponse.<ExamResponse>builder()
                .message("Lấy bài kiểm tra thành công")
                .result(subLessonService.getExam(typeId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/exam/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> createExamLesson(@PathVariable String sectionId, @RequestBody ExamRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.createExam(sectionId, request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/exam/{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> updateExamLesson(@PathVariable String lessonId, @RequestBody ExamRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.updateExam(lessonId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /////////// BÀI VIẾT ///////////////////////////////////////////////////////////
    @GetMapping("/article/{typeId}")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticle(@PathVariable String typeId) {
        ApiResponse<ArticleResponse> response = ApiResponse.<ArticleResponse>builder()
                .message("Lấy video thành công")
                .result(subLessonService.getArticle(typeId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/article/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> createArticle(@PathVariable String sectionId, @RequestBody ArticleLessonRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.createArticle(sectionId, request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/article/{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> updateArticle(@PathVariable String lessonId, @RequestBody String content) {
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .message(lessonService.updateArticle(lessonId, content)).build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    ///////////// LESSON BÀI HỌC ////////////////////////////////////////////////
    @PutMapping("/{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateLesson(@PathVariable String lessonId, @RequestBody LessonRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.updateLesson(lessonId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteLesson(@RequestParam String sectionId, @RequestParam String lessonId) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(lessonService.deleteLesson(sectionId, lessonId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
