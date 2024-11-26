package com.sunny.microservices.course.dto.response.lesson;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.xml.stream.events.Comment;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoResponse {
    String id;
    String videoUrl;
    String thumbnailUrl;
    Double duration;
}
