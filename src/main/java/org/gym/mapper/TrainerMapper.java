package org.gym.mapper;

import org.gym.dto.TrainerDto;
import org.gym.dto.TrainerGetProfileResponse;
import org.gym.dto.TrainerResponse;
import org.gym.entity.Trainer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainingTypeMapper.class})
public interface TrainerMapper {
    TrainerDto convertToDto(Trainer trainer);
    Trainer convertToEntity(TrainerDto trainerDto);
    TrainerGetProfileResponse convertToTrainerGetProfileResponse(TrainerDto trainerDto);
    List<TrainerResponse> convertToTrainerResponseList(List<Trainer> trainerList);
    //TrainerCreateResponse convertToTrainerCreateResponse(Trainer trainer);
}
