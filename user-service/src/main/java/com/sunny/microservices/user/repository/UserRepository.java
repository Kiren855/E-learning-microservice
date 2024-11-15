package com.sunny.microservices.user.repository;

import com.sunny.microservices.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUserId(String userId);
}
