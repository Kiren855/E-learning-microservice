package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.Section;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SectionRepository extends MongoRepository<Section, String> {
}
