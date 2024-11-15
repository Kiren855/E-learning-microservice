package com.sunny.microservices.course.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public abstract class BaseEntity {

    @CreatedDate
    @Field(name = "created_at")
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    protected LocalDateTime updatedAt;

}
