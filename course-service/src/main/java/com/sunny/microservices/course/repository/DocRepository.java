package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.Doc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocRepository extends MongoRepository<Doc, String> {
}
