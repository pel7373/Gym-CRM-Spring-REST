package org.gym.mapper;

import org.gym.dto.*;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainingTypeMapper.class})
public interface TrainerMapper {

    @Named("convertToDto")
    TrainerDto convertToDto(Trainer trainer);

    @Named("convertToEntity")
    Trainer convertToEntity(TrainerDto trainerDto);

    @Mapping(target="userName", expression="java(trainerDto.getUser().getUserName())")
    @Mapping(target="password", expression="java(trainerDto.getUser().getPassword())")
    CreateResponse convertToCreateResponse(TrainerDto trainerDto);

    TrainerSelectResponse convertToTrainerSelectResponse(TrainerDto trainerDto);
    TrainerDto convertTrainerUpdateRequestToTrainerDto(TrainerUpdateRequest trainerUpdateRequest);

    TrainerUpdateResponse convertDtoToUpdateResponse(TrainerDto trainer);

    TrainerForListResponse convertTrainerDtoToTrainerForListResponse(TrainerDto trainer);
    List<TrainerForListResponse> convertTrainerDtoListToTrainerResponseList(List<TrainerDto> trainerDtoList);
    //TrainerCreateResponse convertToTrainerCreateResponse(Trainer trainer);


    default List<TrainerDto> convertTrainersListToTrainerDtoList(List<Trainer> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(this::convertTrainerWithoutTraineesList)
                .toList();
    }

    @Named("convertTrainerWithoutTraineesList")
    @Mapping(target = "trainees", ignore = true)
    TrainerDto convertTrainerWithoutTraineesList(Trainer trainer);

    default List<Trainer> convertTrainersDtoListToTrainerList(List<TrainerDto> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(this::convertTrainerDtoWithoutTraineesList)
                .toList();
    }

    @Named("convertTrainerDtoWithoutTraineesList")
    @Mapping(target = "trainees", ignore = true)
    Trainer convertTrainerDtoWithoutTraineesList(TrainerDto trainerDto);
}
