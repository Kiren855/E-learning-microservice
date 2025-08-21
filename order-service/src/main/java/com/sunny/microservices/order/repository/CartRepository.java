package com.sunny.microservices.order.repository;

import com.sunny.microservices.order.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    List<Cart> findByUserId(String userId);

    Optional<Cart> findByUserIdAndCourseId(String userId, String courseId);

    List<Cart> findByUserIdAndCourseIdIn(String userId, List<String> courseIds);

    boolean existsByUserIdAndCourseId(String userId, String courseId);

}
