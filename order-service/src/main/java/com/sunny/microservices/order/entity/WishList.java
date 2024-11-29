package com.sunny.microservices.order.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("wishlist")
public class WishList {
    @Id
    String id;
    @Indexed
    String userId;
    String courseId;
    String instructorName;
    String courseName;
    Integer price;
}
