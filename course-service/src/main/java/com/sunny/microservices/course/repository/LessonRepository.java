package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.Lesson;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LessonRepository extends MongoRepository<Lesson, String> {
}
