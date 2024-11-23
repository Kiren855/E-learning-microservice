package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.SubTopic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubTopicRepository extends MongoRepository<SubTopic, String> {
}
