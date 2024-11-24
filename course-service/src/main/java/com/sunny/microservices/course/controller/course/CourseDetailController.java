package com.sunny.microservices.course.controller.course;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.course.CourseNotiRequest;
import com.sunny.microservices.course.dto.request.course.CourseOverviewRequest;
import com.sunny.microservices.course.dto.request.course.CourseRequirementRequest;
import com.sunny.microservices.course.dto.request.course.CourseTargetRequest;
import com.sunny.microservices.course.dto.response.course.CourseNotiResponse;
import com.sunny.microservices.course.dto.response.course.CourseOverviewResponse;
import com.sunny.microservices.course.service.course.CourseNotiService;
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

    @PutMapping("/overview/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> updateCourseOverview(@PathVariable String courseId, CourseOverviewRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseOverviewService.updateOverviewCourse(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/overview/{courseId}")
    public ResponseEntity<ApiResponse<CourseOverviewResponse>> getCourseOverview(@PathVariable String courseId) {
            ApiResponse<CourseOverviewResponse> response = ApiResponse.<CourseOverviewResponse>builder()
                    .message("Lấy thông tin tổng quan khoá học thành công")
                    .result(courseOverviewService.getCourseOverview(courseId)).build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/change-price/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> changePrice(@PathVariable String courseId, @RequestBody Integer price) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseOverviewService.updatePrice(courseId, price)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
     ////////////////////////////////////////////////////////////////////////////////////////////
    /////// Requirement ////////////////////////////////////////////////////////////////////////

    RequirementService requirementService;

    @PutMapping("/requirement/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> updateRequirement(@PathVariable String courseId, CourseRequirementRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(requirementService.updateRequirement(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/requirement/{courseId}")
    public ResponseEntity<ApiResponse<List<String>>> getRequirement(@PathVariable String courseId) {
        ApiResponse<List<String>> response = ApiResponse.<List<String>>builder()
                .message("lấy danh sách yêu cầu thành công")
                .result(requirementService.getRequirement(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    /////// Target /////////////////////////////////////////////////////////////////////////////

    TargetService targetService;

    @PutMapping("/target/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> updateTarget(@PathVariable String courseId, CourseTargetRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(targetService.updateTarget(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/target/{courseId}")
    public ResponseEntity<ApiResponse<List<String>>> getTarget(@PathVariable String courseId) {
        ApiResponse<List<String>> response = ApiResponse.<List<String>>builder()
                .message("lấy danh sách mục tiêu thành công")
                .result(targetService.getTarget(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////// Course Notification /////////////////////////////////////////////

    CourseNotiService courseNotiService;
    @GetMapping("/notify/{courseId}")
    public ResponseEntity<ApiResponse<CourseNotiResponse>> getCourseNotify(@PathVariable String courseId) {
            ApiResponse<CourseNotiResponse> response = ApiResponse.<CourseNotiResponse>builder()
                    .message("Lấy thông báo chào mừng vào hoàn thành")
                    .result(courseNotiService.getCourseNoti(courseId)).build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/notify/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> updateCourseNotify(@PathVariable String courseId,
                                                                  @RequestBody CourseNotiRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseNotiService.updateCourseNoti(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ////////// Price //////////////////////////////////////////////////////////////////


}
