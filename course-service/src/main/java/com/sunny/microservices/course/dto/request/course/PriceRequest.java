package com.sunny.microservices.course.dto.request.course;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceRequest {
    Integer price;
}
