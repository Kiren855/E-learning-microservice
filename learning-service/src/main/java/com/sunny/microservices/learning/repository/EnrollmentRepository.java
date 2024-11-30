package com.sunny.microservices.learning.repository;

import com.sunny.microservices.learning.entity.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    List<Enrollment> findByUserId(String userId);
}
