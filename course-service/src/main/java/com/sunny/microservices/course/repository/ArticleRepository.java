package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {
}
