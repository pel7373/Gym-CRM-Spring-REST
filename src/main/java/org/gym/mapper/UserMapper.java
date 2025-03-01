package org.gym.mapper;

import org.gym.dto.*;
import org.gym.dto.request.user.UserUpdateRequest;
import org.gym.dto.response.user.UserForListResponse;
import org.gym.dto.response.user.UserUpdateResponse;
import org.gym.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto convertToDto(User user);
    User convertToEntity(UserDto userDto);

    @Mapping(target="userName", expression="java(user.getUserName())")
    UserUpdateResponse convertUserDtoToUserUpdateResponse(UserDto user);

    @Mapping(target="userName", expression="java(user.getUserName())")
    UserUpdateResponse convertUserToUserUpdateResponse(User user);

    UserForListResponse convertDtoToForListResponse(User user);
    //    UserForListResponse convertToUserForListResponse(UserDto user);
}
