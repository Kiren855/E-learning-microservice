package com.sunny.microservices.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    @Size(min = 4, message = "INVALID_USERNAME")
    @NotBlank(message = "USERNAME_REQUIRED")
    @Pattern(regexp = "^[a-zA-Z].*", message = "USERNAME_MUST_START_WITH_LETTER")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    @NotBlank(message = "PASSWORD_REQUIRED")
    String password;


    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "INVALID_EMAIL")
    String email;


    @NotBlank(message = "FIRSTNAME_REQUIRED")
    String firstName;
    @NotBlank(message = "LASTNAME_REQUIRED")
    String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dob;

}
