package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.PendingCourse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PendingCourseRepository extends MongoRepository<PendingCourse, String> {
    Optional<PendingCourse> findByCourseId(String courseId);
}
