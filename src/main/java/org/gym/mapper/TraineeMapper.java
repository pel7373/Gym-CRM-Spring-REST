package org.gym.mapper;

import org.gym.dto.*;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.entity.Trainee;
import org.gym.entity.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TraineeMapper {

    @Mapping(target = "trainers", ignore = true)
    TraineeDto convertToDto(Trainee trainee);

    @Mapping(target = "trainers", ignore = true)
    Trainee convertToEntity(TraineeDto traineeDto);

    @Mapping(target="userName", expression="java(trainee.getUser().getUserName())")
    @Mapping(target="password", expression="java(trainee.getUser().getPassword())")
    CreateResponse convertToCreateResponse(Trainee trainee);

    TraineeSelectResponse convertTraineeToTraineeSelectResponse(Trainee trainee);
    TraineeUpdateResponse convertTraineeToTraineeUpdateResponse(Trainee trainee);

    default String convertToString(TrainingType trainingType) {
        if(trainingType == null) return null;
        return trainingType.getTrainingTypeName();
    }
}
