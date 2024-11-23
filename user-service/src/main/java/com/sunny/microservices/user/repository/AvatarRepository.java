package com.sunny.microservices.user.repository;

import com.sunny.microservices.user.entity.Avatar;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AvatarRepository extends MongoRepository<Avatar, String> {
    Optional<Avatar> findByName(String name);
}
