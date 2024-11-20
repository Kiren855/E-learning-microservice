package com.sunny.microservices.course.service;


import com.sunny.microservices.basedomain.course.dto.DTO.ReviewDetail;
import com.sunny.microservices.course.client.UserClient;
import com.sunny.microservices.course.dto.request.ReviewRequest;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.entity.Review;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.CourseRepository;
import com.sunny.microservices.course.repository.ReviewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    ReviewRepository reviewRepository;
    CourseRepository courseRepository;
    UserClient userClient;

    public String createReview(String courseId, ReviewRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Review review = Review.builder()
                .userId(userId)
                .rating(request.getRating())
                .content(request.getContent()).build();

        reviewRepository.save(review);

        Double rating = course.getRating();
        Integer totalReview = course.getReviews().size();
        Double totalRating = rating * totalReview;

        totalReview += 1;
        totalRating += request.getRating();
        course.setRating(1.0 * totalRating / totalReview);
        course.getReviews().add(review.getId());
        courseRepository.save(course);

        return "thêm đánh giá thành công";
    }

    public String updateReview(String reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        review.setRating(review.getRating());
        review.setContent(request.getContent());

        reviewRepository.save(review);
        return "Cập nhật đánh giá thành công";
    }

    public List<ReviewDetail> findReviewsById(List<String> ids) {
        List<Review> reviews = reviewRepository.findAllById(ids);

        return reviews.stream().map(review -> {
                  String username = userClient.getProfile(review.getUserId()).getUsername();
                   return ReviewDetail.builder()
                            .username(username)
                            .rating(review.getRating())
                            .content(review.getContent()).build();
                })
                .collect(Collectors.toList());
    }
}

