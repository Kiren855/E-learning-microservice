package com.sunny.microservices.course.dto.response;

import com.sunny.microservices.course.entity.Section;
import com.sunny.microservices.course.entity.Topic;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  CourseResponse {
    String id;
    String image;
    String title;
    String subTitle;
    String description;
    List<Topic> topic;
    String instructor;
    List<Section> sections;
    Double rating;
    List<String> language;
    Double price;
    Double discount;
    Boolean isDraft;
    List<String> review;
    List<String> targetAudiences;
    List<String> requirements;
}
