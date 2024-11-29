package com.sunny.microservices.payment.repository;

import com.sunny.microservices.payment.entity.PaymentHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentHistoryRepository extends MongoRepository<PaymentHistory, String> {
    Optional<PaymentHistory> findByOrderId(String orderId);
}
