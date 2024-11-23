package com.sunny.microservices.course.controller.course;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.course.CourseOverviewRequest;
import com.sunny.microservices.course.dto.request.course.CourseRequirementRequest;
import com.sunny.microservices.course.dto.request.course.CourseTargetRequest;
import com.sunny.microservices.course.dto.response.course.CourseOverviewResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.service.course.CourseOverviewService;
import com.sunny.microservices.course.service.course.RequirementService;
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
public class CourseDetailController {

    ////////////////////////////////////////////////////////////////////////////////////////////
    /////// Couse Overview /////////////////////////////////////////////////////////////////////

    CourseOverviewService courseOverviewService;

    @PostMapping("/overview/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> updateCourseOverview(@PathVariable String courseId, CourseOverviewRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseOverviewService.updateOverviewCourse(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/overview/{courseId}")
    public ResponseEntity<?> getCourseOverview(@PathVariable String courseId) {
            ApiResponse<CourseOverviewResponse> response = ApiResponse.<CourseOverviewResponse>builder()
                    .message("Lấy thông tin tổng quan khoá học thành công")
                    .result(courseOverviewService.getCourseOverview(courseId)).build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
    }
     ////////////////////////////////////////////////////////////////////////////////////////////
    /////// Requirement ////////////////////////////////////////////////////////////////////////

    RequirementService requirementService;

    @PostMapping("/requirement/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> createRequirement(@PathVariable String courseId, CourseRequirementRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(requirementService.createRequirement(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/requirement/{courseId}")
    public ResponseEntity<?> getRequirement(@PathVariable String courseId) {
        ApiResponse<List<String>> response = ApiResponse.<List<String>>builder()
                .message("lấy danh sách yêu cầu thành công")
                .result(requirementService.getRequirement(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    /////// Target /////////////////////////////////////////////////////////////////////////////

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
