package com.sunny.microservices.course.dto.response;


import com.sunny.microservices.course.dto.DTO.SectionDetail;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDetailResponse {
    String id;
    List<SectionDetail> sections;
}
