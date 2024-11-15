package com.sunny.microservices.user.mapper;

import com.sunny.microservices.user.dto.request.RegistrationRequest;
import com.sunny.microservices.user.dto.response.UserResponse;
import com.sunny.microservices.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toProfile(RegistrationRequest request);

    UserResponse toProfileResponse(User user);
}
