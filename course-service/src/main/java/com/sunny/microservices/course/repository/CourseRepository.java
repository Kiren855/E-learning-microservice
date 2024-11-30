package com.sunny.microservices.course.repository;

import com.sunny.microservices.course.entity.Course;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {
    @Query("{ '_id': ?0 }, { '$push': { 'sections': ?1 } }")
    void updateSections(String courseId, ObjectId sectionId);

    List<Course> findByInstructorId(String instructor);

    List<Course> findByMainTopic(String topicId);
}
