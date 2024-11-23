package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.MainTopic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MainTopicRepository extends MongoRepository<MainTopic, String> {
}
