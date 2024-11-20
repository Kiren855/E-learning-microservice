package com.sunny.microservices.course.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("courses")
public class Course extends BaseEntity {
    @Id
    String id;
    String image;
    String title;
    String subTitle;
    String description;
    List<String> topic;
    String instructor;
    List<String> sections;
    Double rating;
    List<String> language;
    Double price;
    Double discount;
    Boolean isDraft;
    List<String> reviews;
    List<String> targetAudiences;
    List<String> requirements;
}
