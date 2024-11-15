package com.sunny.microservices.course.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("lessons")
public class Lesson {
    @Id
    String id;
    String name;
    String type;
    String type_id;
    Integer partNumber;
    List<String> comments;
}
