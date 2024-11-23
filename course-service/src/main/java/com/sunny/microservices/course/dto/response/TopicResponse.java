package com.sunny.microservices.course.dto.response;

import com.sunny.microservices.course.dto.DTO.TopicPreview;
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
public class TopicResponse {
    String id;
    String name;
    List<TopicPreview> subtopics;
}
