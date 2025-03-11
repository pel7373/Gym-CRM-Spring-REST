package org.gym.mapper;

import org.gym.dto.*;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainingTypeMapper.class})
public interface TrainerMapper {

    @Mapping(target = "trainees", ignore = true)
    TrainerDto convertToDto(Trainer trainer);

    @Mapping(target = "trainees", ignore = true)
    Trainer convertToEntity(TrainerDto trainerDto);

    @Mapping(target="userName", expression="java(trainer.getUser().getUserName())")
    @Mapping(target="password", expression="java(trainer.getUser().getPassword())")
    CreateResponse convertToCreateResponse(Trainer trainer);

    @Mapping(target = "specialization", source = "specialization.trainingTypeName")
    TrainerSelectResponse convertToTrainerSelectResponse(Trainer trainer);

    @Mapping(target = "specialization", expression="java(trainer.getSpecialization().getTrainingTypeName())")
    TrainerUpdateResponse convertTrainerToTrainerUpdateResponse(Trainer trainer);

    @Mapping(target = "specialization", expression="java(trainer.getSpecialization().getTrainingTypeName())")
    TrainerForListResponse convertTrainerToTrainerForListResponse(Trainer trainer);

    List<TrainerForListResponse> convertTrainerListToTrainerForListResponseList(List<Trainer> trainerList);
}
