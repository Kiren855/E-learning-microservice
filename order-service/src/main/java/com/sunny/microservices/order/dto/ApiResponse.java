package com.sunny.microservices.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response structure")
public class ApiResponse<T> {
    @Builder.Default
    @Schema(description = "Response code", example = "1000")
    private int code = 1000;

    @Schema(description = "Response message", example = "Operation successful")
    private String message;

    @Schema(description = "Response result")
    private T result;
}
