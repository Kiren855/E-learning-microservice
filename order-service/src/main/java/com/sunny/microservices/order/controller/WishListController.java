package com.sunny.microservices.order.controller;


import com.sunny.microservices.order.dto.ApiResponse;
import com.sunny.microservices.order.dto.request.WishCourseRequest;
import com.sunny.microservices.order.dto.response.AWishResponse;
import com.sunny.microservices.order.dto.response.WishListResponse;
import com.sunny.microservices.order.service.WishListService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("wish-list")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WishListController {
    WishListService wishListService;

    @GetMapping()
    public ResponseEntity<ApiResponse<WishListResponse>> getCourseInWishList(){
            ApiResponse<WishListResponse> response = ApiResponse.<WishListResponse>builder()
                    .message("lấy danh sách ước thành công")
                    .result(wishListService.getCoursesInWishList()).build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<AWishResponse>> addCourseToWishList(@RequestBody WishCourseRequest request) {
        ApiResponse<AWishResponse> response = ApiResponse.<AWishResponse>builder()
                .message("thêm vào danh sách ước thành công")
                .result(wishListService.addCourseToWishList(request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/move-to-cart/{courseId}")
    public ResponseEntity<ApiResponse<String>> moveToCart(@PathVariable String courseId) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(wishListService.changeToCart(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<String>> removeCourseFormWishList(@PathVariable String courseId) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(wishListService.removeCourseFromWishList(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
