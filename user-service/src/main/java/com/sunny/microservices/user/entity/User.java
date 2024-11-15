package com.sunny.microservices.user.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("user")
public class User {
    @MongoId
    String id;
    // UserId from keycloak
    String userId;
    String email;
    String username;
    String firstName;
    String lastName;
    String googleId;
    String avatar;
    LocalDate dob;
    String introduce;
}
