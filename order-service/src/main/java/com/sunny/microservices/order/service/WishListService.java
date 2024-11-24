package com.sunny.microservices.order.service;

import com.sunny.microservices.order.dto.request.WishCourseRequest;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishListService {
    WishListRepository wishListRepository;
    CartRepository cartRepository;

    public String addCourseToWishList(WishCourseRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<WishList> wish = wishListRepository.findByUserIdAndCourseId(userId, request.getCourseId());
        if(wish.isPresent()) {
            throw new AppException(ErrorCode.COURSE_EXISTS);
        }else {
            WishList newWish = WishList.builder()
                    .userId(userId)
                    .courseId(request.getCourseId())
                    .courseName(request.getCourseName())
                    .price(request.getPrice()).build();
            wishListRepository.save(newWish);
        }
        return "Thêm khoá học vào danh sách ước thành công";
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

    public List<WishList> getCoursesInWishList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<WishList> wishs = wishListRepository.findByUserId(userId);
        if (wishs == null) {
            return new ArrayList<>();
        }
        return wishs;
    }

    public String changeToCart(String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<WishList> wish = wishListRepository.findByUserIdAndCourseId(userId, courseId);
        if (wish.isPresent()) {
            Cart cart = Cart.builder()
                    .userId(userId)
                    .courseId(courseId)
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
