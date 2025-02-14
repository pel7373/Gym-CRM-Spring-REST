package org.gym.mapper;

import org.gym.dto.*;
import org.gym.dto.request.trainee.TraineeCreateRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeForListResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.entity.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TraineeMapper {
    TraineeDto convertToDto(Trainee trainee);
    Trainee convertToEntity(TraineeDto traineeDto);

    TraineeDto convertTraineeUpdateRequestToTraineeDto(TraineeUpdateRequest traineeUpdateRequest);

    @Mapping(target="userName", expression="java(traineeDto.getUser().getUserName())")
    @Mapping(target="password", expression="java(traineeDto.getUser().getPassword())")
    CreateResponse convertToCreateResponse(TraineeDto traineeDto);

    TraineeUpdateResponse convertDtoToUpdateResponse(TraineeDto trainee);

    @Mapping(target="userName", expression="java(traineeDto.getUser().getUserName())")
//    @Mapping(target="firstName", expression="java(traineeDto.getUser().getFirstName())")
//    @Mapping(target="lastName", expression="java(traineeDto.getUser().getLastName())")
    TraineeForListResponse convertToTraineeForListResponse(TraineeDto traineeDto);

    //CreateResponse convertToTraineeCreateResponse(Trainee trainee);
    TraineeDto convertCreateRequestToDto(TraineeCreateRequest trainee);
    TraineeSelectResponse convertToTraineeSelectResponse(TraineeDto traineeDto);
}
