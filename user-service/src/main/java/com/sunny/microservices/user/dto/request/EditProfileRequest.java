package com.sunny.microservices.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditProfileRequest {
    @Size(min = 4, message = "INVALID_FIRSTNAME")
    String firstName;

    @Size(min = 4, message = "INVALID_LASTNAME")
    String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dob;

    MultipartFile avatar;

    String introduce;
}
