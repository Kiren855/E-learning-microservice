package com.sunny.microservices.order.service;

import com.sunny.microservices.order.dto.request.WishCourseRequest;
import com.sunny.microservices.order.dto.response.ACartResponse;
import com.sunny.microservices.order.dto.response.AWishResponse;
import com.sunny.microservices.order.dto.response.WishListResponse;
import com.sunny.microservices.order.entity.Cart;
import com.sunny.microservices.order.entity.WishList;
import com.sunny.microservices.order.exception.AppException;
import com.sunny.microservices.order.exception.ErrorCode;
import com.sunny.microservices.order.repository.CartRepository;
import com.sunny.microservices.order.repository.WishListRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishListService {
    WishListRepository wishListRepository;
    CartRepository cartRepository;

    public AWishResponse addCourseToWishList(WishCourseRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<WishList> wish = wishListRepository.findByUserIdAndCourseId(userId, request.getCourseId());
        if(wish.isPresent()) {
            throw new AppException(ErrorCode.COURSE_EXISTS);
        }else {
            WishList newWish = WishList.builder()
                    .userId(userId)
                    .courseId(request.getCourseId())
                    .image(request.getImage())
                    .instructorName(request.getInstructorName())
                    .courseName(request.getCourseName())
                    .price(request.getPrice()).build();
            wishListRepository.save(newWish);

            return AWishResponse.builder()
                    .id(newWish.getId())
                    .userId(newWish.getUserId())
                    .courseId(newWish.getCourseId())
                    .image(newWish.getImage())
                    .courseName(newWish.getCourseName())
                    .instructorName(newWish.getInstructorName())
                    .price(newWish.getPrice()).build();
        }
    }

    public String removeCourseFromWishList(String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<WishList> wish = wishListRepository.findByUserIdAndCourseId(userId, courseId);
        if (wish.isPresent()) {
            wishListRepository.delete(wish.get());
        }else {
            throw new AppException(ErrorCode.COURSE_NOT_EXISTS);
        }
        return "Xoá khoá học khỏi danh sách ước thành công";
    }

    public WishListResponse getCoursesInWishList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<WishList> wishs = wishListRepository.findByUserId(userId);
        List<AWishResponse> wishResponses = wishs.stream().map(wish -> AWishResponse.builder()
                .id(wish.getId())
                .courseId(wish.getCourseId())
                .userId(wish.getUserId())
                .image(wish.getImage())
                .instructorName(wish.getInstructorName())
                .courseName(wish.getCourseName())
                .price(wish.getPrice()).build()).toList();

        return WishListResponse.builder().wishList(Objects.requireNonNullElseGet(wishResponses, ArrayList::new)).build();
    }

    public String changeToCart(String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        log.info("courseID {}", courseId);
        Optional<WishList> wish = wishListRepository.findByUserIdAndCourseId(userId, courseId);
        if (wish.isPresent()) {
            Cart cart = Cart.builder()
                    .userId(userId)
                    .courseId(courseId)
                    .image(wish.get().getImage())
                    .instructorName(wish.get().getInstructorName())
                    .courseName(wish.get().getCourseName())
                    .price(wish.get().getPrice()).build();
            cartRepository.save(cart);
            wishListRepository.delete(wish.get());
        }else {
            throw new AppException(ErrorCode.COURSE_NOT_EXISTS);
        }
        return "Chuyển khoá học vào giỏ hàng thành công";
    }


}
