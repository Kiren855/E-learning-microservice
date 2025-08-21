package com.sunny.microservices.order.repository;

import com.sunny.microservices.order.entity.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends MongoRepository<PaymentStatus, String> {
    boolean existsByUserIdAndCourseId(String userId, String courseId);
}
