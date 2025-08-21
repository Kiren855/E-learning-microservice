package com.sunny.microservices.course.controller.course;

import com.azure.core.annotation.Get;
import com.azure.core.annotation.Put;
import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.DTO.SubmitCourseDto;
import com.sunny.microservices.course.dto.request.course.CourseCreateRequest;
import com.sunny.microservices.course.dto.request.course.CourseRequest;
import com.sunny.microservices.course.dto.response.IdResponse;
import com.sunny.microservices.course.dto.response.course.CoursePreviewResponse;
import com.sunny.microservices.course.dto.response.course.CourseResponse;
import com.sunny.microservices.course.dto.response.course.CourseSearchResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.service.course.CourseCreateService;
import com.sunny.microservices.course.service.course.CourseSearchService;
import com.sunny.microservices.course.service.course.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseController {

    CourseService courseService;
    CourseCreateService courseCreateService;
    CourseSearchService courseSearchService;
    @GetMapping("/preview/{courseId}")
    public ResponseEntity<ApiResponse<CoursePreviewResponse>> getCourse(@PathVariable String courseId) {
        ApiResponse<CoursePreviewResponse> response = ApiResponse.<CoursePreviewResponse>builder()
                .message("lấy thông tin khoá học thành công")
                .result(courseService.getCoursePreview(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping()
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<IdResponse>> createCourse(@RequestBody CourseCreateRequest request) {
        ApiResponse<IdResponse> response = ApiResponse.<IdResponse>builder()
                .message("tạo khoá học thành côngb ")
                .result(courseCreateService.createCourse(request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>>  deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message("Xoá khoá học thành công").build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping()
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getCourses() {
        ApiResponse<List<CourseResponse>> response = ApiResponse.<List<CourseResponse>>builder()
                .message("Lấy danh sách khoá học thành công")
                .result(courseService.getCourses()).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/submit/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> submitCourse(@PathVariable String courseId) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseService.submitCourse(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("admin/approve/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> approveCourse(@PathVariable String courseId) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseService.approveCourse(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("admin/reject/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> rejectCourse(@PathVariable String courseId,
                                                            @RequestBody String reason) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseService.rejectCourse(courseId, reason)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/admin/course-submited")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SubmitCourseDto>>> getListCourseSubmited() {
        ApiResponse<List<SubmitCourseDto>> response = ApiResponse.<List<SubmitCourseDto>>builder()
                .message("Lấy danh sách chờ khoá học chấp thuận thành công")
                .result(courseService.getListSubmitCourse()).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CourseSearchResponse>> searchCoursesByTitle(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int p
    ) {
        Page<CourseSearchResponse> result = courseSearchService.searchCourseByTitle(keyword, p);
        return ResponseEntity.ok(result);
    }

}
