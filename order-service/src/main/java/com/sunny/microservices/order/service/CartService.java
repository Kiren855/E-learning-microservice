package com.sunny.microservices.order.service;

import com.sunny.microservices.order.dto.request.CartCourseRequest;
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
public class CartService {
    CartRepository cartRepository;
    WishListRepository wishListRepository;
    public String addCourseToCart(CartCourseRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<Cart> cart = cartRepository.findByUserIdAndCourseId(userId, request.getCourseId());
        if(cart.isPresent()) {
             throw new AppException(ErrorCode.COURSE_EXISTS);
        }else {
            Cart newCart = Cart.builder()
                    .userId(userId)
                    .courseId(request.getCourseId())
                    .courseName(request.getCourseName())
                    .price(request.getPrice()).build();
            cartRepository.save(newCart);
        }
        return "Thêm khoá học vào giỏ hàng thành công";
    }

    public String removeCourseFromCart(String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<Cart> cart = cartRepository.findByUserIdAndCourseId(userId, courseId);
        if (cart.isPresent()) {
            cartRepository.delete(cart.get());
        }else {
            throw new AppException(ErrorCode.COURSE_NOT_EXISTS);
        }
        return "Xoá khoá học khỏi giỏ hàng thành công";
    }

    public List<Cart> getCoursesInCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<Cart> carts = cartRepository.findByUserId(userId);
        if (carts == null) {
            return new ArrayList<>();
        }
        return carts;
    }

    public String changeToWishList(String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<Cart> cart = cartRepository.findByUserIdAndCourseId(userId, courseId);
        if (cart.isPresent()) {
            WishList wish = WishList.builder()
                    .userId(userId)
                    .courseId(courseId)
                    .courseName(cart.get().getCourseName())
                    .price(cart.get().getPrice()).build();
            wishListRepository.save(wish);
            cartRepository.delete(cart.get());
        }else {
            throw new AppException(ErrorCode.COURSE_NOT_EXISTS);
        }
        return "Chuyển khoá học vào giỏ hàng thành công";
    }
}
