package com.sunny.microservices.order.controller;

import com.sunny.microservices.order.dto.ApiResponse;
import com.sunny.microservices.order.dto.request.CartCourseRequest;
import com.sunny.microservices.order.dto.response.ACartResponse;
import com.sunny.microservices.order.dto.response.CartResponse;
import com.sunny.microservices.order.entity.Cart;
import com.sunny.microservices.order.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {
    CartService cartService;

    @GetMapping()
    public ResponseEntity<ApiResponse<CartResponse>> getCourseInCart(){
        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .message("lấy giỏ hàng thành công")
                .result(cartService.getCoursesInCart()).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ACartResponse>> addCourseToCart(@RequestBody CartCourseRequest request) {
        ApiResponse<ACartResponse> response = ApiResponse.<ACartResponse>builder()
                .message("thêm vào giỏ hàng thành công")
                .result(cartService.addCourseToCart(request)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/move-to-wishlist/{courseId}")
    public ResponseEntity<ApiResponse<String>> moveToWishList(@PathVariable String courseId) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(cartService.changeToWishList(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<String>> removeCourseFormCart(@PathVariable String courseId) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(cartService.removeCourseFromCart(courseId)).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
