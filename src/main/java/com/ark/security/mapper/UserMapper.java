package com.ark.security.mapper;

import com.ark.security.dto.request.UserInfoRequest;
import com.ark.security.dto.request.UserRequest;
import com.ark.security.dto.response.UserResponse;
import com.ark.security.models.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    void updateUser(@MappingTarget User user, UserRequest userRequest);
    void updateUserInfo(@MappingTarget User user, UserInfoRequest userInfoRequest);
    UserResponse toUserResponse(User user);
}
