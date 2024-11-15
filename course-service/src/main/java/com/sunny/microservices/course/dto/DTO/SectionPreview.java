package com.sunny.microservices.course.dto.DTO;

import com.sunny.microservices.course.entity.Lesson;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;

import java.time.Duration;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionPreview {
    String name;
    Integer partNumber;
    List<String> lessons;
    Double duration;
    Integer totalLesson;
}
