package org.gym.mapper;

import org.gym.dto.*;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeForListResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.entity.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainingTypeMapper.class})
public interface TrainerMapper {
    TrainerDto convertToDto(Trainer trainer);
    Trainer convertToEntity(TrainerDto trainerDto);

    @Mapping(target="userName", expression="java(trainerDto.getUser().getUserName())")
    @Mapping(target="password", expression="java(trainerDto.getUser().getPassword())")
    CreateResponse convertToCreateResponse(TrainerDto trainerDto);

    @Mapping(target="userName", expression="java(trainerDto.getUser().getUserName())")
    @Mapping(target="firstName", expression="java(trainerDto.getUser().getFirstName())")
    @Mapping(target="lastName", expression="java(trainerDto.getUser().getLastName())")
    TrainerForListResponse convertToTrainerForListResponse(TrainerDto trainerDto);

    TrainerSelectResponse convertToTrainerGetProfileResponse(TrainerDto trainerDto);
    List<TrainerForListResponse> convertToTrainerResponseList(List<Trainer> trainerList);
    //TrainerCreateResponse convertToTrainerCreateResponse(Trainer trainer);
}
