package com.sunny.microservices.course.controller.course;

import com.sunny.microservices.basedomain.course.dto.response.CourseLearningResponse;
import com.sunny.microservices.course.service.course.CourseClientService;
import com.sunny.microservices.course.service.course.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    CourseClientService courseClientService;

    @GetMapping("/detail/{courseId}")
    public CourseLearningResponse getCourse(@PathVariable String courseId) {
        return courseClientService.getCourseDetail(courseId);
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
