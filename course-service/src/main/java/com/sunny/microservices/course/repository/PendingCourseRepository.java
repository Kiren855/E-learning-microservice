package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.PendingCourse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PendingCourseRepository extends MongoRepository<PendingCourse, String> {
}
