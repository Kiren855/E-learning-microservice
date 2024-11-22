package com.sunny.microservices.course.dto.response.course;

import com.sunny.microservices.basedomain.course.dto.DTO.TopicDetail;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
    List<TopicDetail> topic;
    Boolean isDraft;
}
