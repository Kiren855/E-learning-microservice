package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExamRepository extends MongoRepository<Exam, String> {
}
