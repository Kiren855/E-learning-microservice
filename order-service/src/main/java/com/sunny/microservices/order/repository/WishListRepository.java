package com.sunny.microservices.order.repository;

import com.sunny.microservices.order.entity.Cart;
import com.sunny.microservices.order.entity.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends MongoRepository<WishList, String> {
    List<WishList> findByUserId(String userId);

    Optional<WishList> findByUserIdAndCourseId(String userId, String courseId);
}
