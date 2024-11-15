package com.sunny.microservices.course.dto.response;

import com.sunny.microservices.course.entity.Lesson;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionResponse {
    String id;
    String name;
    Integer partNumber;
    List<Lesson> lessons;
    Long duration;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
