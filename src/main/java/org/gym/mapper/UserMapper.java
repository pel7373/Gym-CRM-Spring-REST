package org.gym.mapper;

import org.gym.dto.TraineeCreateRequest;
import org.gym.dto.TraineeDto;
import org.gym.dto.UserCreateRequest;
import org.gym.dto.UserDto;
import org.gym.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto convertToDto(User user);
    User convertToEntity(UserDto userDto);
    UserDto convertCreateRequestToDto(UserCreateRequest user);
}
