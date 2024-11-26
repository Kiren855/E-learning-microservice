package com.sunny.microservices.course.controller.course;

import com.sunny.microservices.course.dto.ApiResponse;
import com.sunny.microservices.course.dto.request.course.CourseNotiRequest;
import com.sunny.microservices.course.dto.request.course.CourseOverviewRequest;
import com.sunny.microservices.course.dto.request.course.PriceRequest;
import com.sunny.microservices.course.dto.request.course.TargetAndRequireRequest;
import com.sunny.microservices.course.dto.response.course.*;
import com.sunny.microservices.course.service.course.*;
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
    TargetAndRequireService targetAndRequireService;

    @PutMapping("/overview/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<CourseOverviewResponse>> updateCourseOverview(@PathVariable String courseId, CourseOverviewRequest request) {
        ApiResponse<CourseOverviewResponse> response = ApiResponse.<CourseOverviewResponse>builder()
                .message("Cập nhật tổng quan khoá học thành công")
                .result(courseOverviewService.updateOverviewCourse(courseId, request)).build();

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
    public ResponseEntity<ApiResponse<String>> changePrice(@PathVariable String courseId, @RequestBody PriceRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(courseOverviewService.updatePrice(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get-price/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<PriceResponse>> getPrice(@PathVariable String courseId) {
        ApiResponse<PriceResponse> response = ApiResponse.<PriceResponse>builder()
                .message("lấy giá tiền khoá học thành công")
                .result(courseOverviewService.getPrice(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

     ////////////////////////////////////////////////////////////////////////////////////////////
    /////// Requirement And require////////////////////////////////////////////////////////////////////////

    @PutMapping("/target-require/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> updateTargetAndRequire(@PathVariable String courseId,
                                                                      @RequestBody TargetAndRequireRequest request) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(targetAndRequireService.updateTargetAndRequired(courseId, request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/target-require/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<TargetAndRequireResponse>> getTargetAndRequire(@PathVariable String courseId) {
        ApiResponse<TargetAndRequireResponse> response = ApiResponse.<TargetAndRequireResponse>builder()
                .message("lấy danh sách mục tiêu và yêu cầu thành công")
                .result(targetAndRequireService.getTargetAndRequire(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    ////////////////////////////////////////////////////////////////////////////
    ////////// Course Notification /////////////////////////////////////////////

    CourseNotiService courseNotiService;
    @GetMapping("/notify/{courseId}")
    public ResponseEntity<ApiResponse<CourseNotiResponse>> getCourseNotify(@PathVariable String courseId) {
            ApiResponse<CourseNotiResponse> response = ApiResponse.<CourseNotiResponse>builder()
                    .message("Lấy thông báo chào mừng và hoàn thành")
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
    /////////////////////////////////////////////////////////////////////////////
    ////// GET FULL DETAIL ABOUT COURSE /////////////////////////////////////////

    @GetMapping("/full-detail/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> getFullDetailCourse(@PathVariable String courseId) {
        ApiResponse<CourseDetailResponse> response = ApiResponse.<CourseDetailResponse>builder()
                .message("Lấy toàn bộ thông tin về khoá học thành công")
                .result(courseOverviewService.getCourseDetail(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
