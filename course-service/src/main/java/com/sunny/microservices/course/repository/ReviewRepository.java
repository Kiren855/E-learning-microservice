package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
}
