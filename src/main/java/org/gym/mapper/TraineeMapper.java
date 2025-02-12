package org.gym.mapper;

import org.gym.dto.*;
import org.gym.entity.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TraineeMapper {
    TraineeDto convertToDto(Trainee trainee);
    Trainee convertToEntity(TraineeDto traineeDto);

    @Mapping(target="userName", expression="java(traineeDto.getUser().getUserName())")
    @Mapping(target="firstName", expression="java(traineeDto.getUser().getFirstName())")
    @Mapping(target="lastName", expression="java(traineeDto.getUser().getLastName())")
    TraineeForListResponse convertToTraineeForListResponse(TraineeDto traineeDto);

    TraineeCreateResponse convertToTraineeCreateResponse(Trainee trainee);
    TraineeDto convertCreateRequestToDto(TraineeCreateRequest trainee);
    TraineeProfileResponse convertToTraineeProfileResponse(TraineeDto traineeDto);
}
