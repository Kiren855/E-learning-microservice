package com.sunny.microservices.order.service;

import com.sunny.microservices.basedomain.event.PaymentEvent;
import com.sunny.microservices.order.dto.request.CartCourseRequest;
import com.sunny.microservices.order.dto.response.ACartResponse;
import com.sunny.microservices.order.dto.response.CartResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    WishListRepository wishListRepository;
    public ACartResponse addCourseToCart(CartCourseRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<Cart> cart = cartRepository.findByUserIdAndCourseId(userId, request.getCourseId());
        if(cart.isPresent()) {
             throw new AppException(ErrorCode.COURSE_EXISTS);
        }else {
            Cart newCart = Cart.builder()
                    .userId(userId)
                    .instructorName(request.getInstructorName())
                    .courseId(request.getCourseId())
                    .courseName(request.getCourseName())
                    .price(request.getPrice()).build();
            cartRepository.save(newCart);

            return ACartResponse.builder()
                    .id(newCart.getId())
                    .userId(newCart.getUserId())
                    .courseId(newCart.getCourseId())
                    .instructorName(newCart.getInstructorName())
                    .courseName(newCart.getCourseName())
                    .price(newCart.getPrice()).build();
        }
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

    public CartResponse getCoursesInCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<Cart> carts = cartRepository.findByUserId(userId);
        List<ACartResponse> cartResponses = carts.stream().map(cart -> ACartResponse.builder()
                .id(cart.getId())
                .courseId(cart.getCourseId())
                .userId(cart.getUserId())
                .instructorName(cart.getInstructorName())
                .courseName(cart.getCourseName())
                .price(cart.getPrice()).build()).toList();
        return CartResponse.builder().cart(Objects.requireNonNullElseGet(cartResponses, ArrayList::new)).build();
    }

    public String changeToWishList(String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<Cart> cart = cartRepository.findByUserIdAndCourseId(userId, courseId);
        if (cart.isPresent()) {
            WishList wish = WishList.builder()
                    .userId(userId)
                    .courseId(courseId)
                    .instructorName(cart.get().getInstructorName())
                    .courseName(cart.get().getCourseName())
                    .price(cart.get().getPrice()).build();
            wishListRepository.save(wish);
            cartRepository.delete(cart.get());
        }else {
            throw new AppException(ErrorCode.COURSE_NOT_EXISTS);
        }
        return "Chuyển khoá học vào giỏ hàng thành công";
    }

    @Transactional
    public void removeCourseOrderedFromCart(String userId, List<PaymentEvent.Course> courses) {
        List<String> courseIds = courses.stream().map(PaymentEvent.Course::getCourseId).toList();
        List<Cart> carts = cartRepository.findByUserIdAndCourseIdIn(userId, courseIds);

        if (carts.size() < courses.size()) {
            throw new AppException(ErrorCode.COURSE_NOT_EXISTS);
        }

        cartRepository.deleteAll(carts);
    }
}
