package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, String> {
}
