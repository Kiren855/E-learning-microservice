package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.Video;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface VideoRepository extends MongoRepository<Video, String> {
}
