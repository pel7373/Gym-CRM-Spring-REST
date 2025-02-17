package org.gym.mapper;

import org.gym.dto.*;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeForListResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.entity.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TraineeMapper {

    @Named("convertToDto")
    TraineeDto convertToDto(Trainee trainee);

    @Named("convertToEntity")
    Trainee convertToEntity(TraineeDto traineeDto);

    @Mapping(target="userName", expression="java(traineeDto.getUser().getUserName())")
    @Mapping(target="password", expression="java(traineeDto.getUser().getPassword())")
    CreateResponse convertToCreateResponse(TraineeDto traineeDto);

    TraineeSelectResponse convertToTraineeSelectResponse(TraineeDto traineeDto);
    TraineeDto convertTraineeUpdateRequestToTraineeDto(TraineeUpdateRequest traineeUpdateRequest);
    TraineeUpdateResponse convertDtoToUpdateResponse(TraineeDto trainee);
    TraineeForListResponse convertToTraineeForListResponse(TraineeDto traineeDto);

    default List<TraineeDto> convertTraineesListToTraineeDtoList(List<Trainee> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(this::convertTraineeWithoutTrainersList)
                .toList();
    }

    @Named("convertTraineeWithoutTrainersList")
    @Mapping(target = "trainers", ignore = true)
    TraineeDto convertTraineeWithoutTrainersList(Trainee trainee);

    default List<Trainee> convertTraineesDtoListToTraineeList(List<TraineeDto> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(this::convertTraineeDtoWithoutTrainersList)
                .toList();
    }

    @Named("convertTraineeDtoWithoutTrainersList")
    @Mapping(target = "trainers", ignore = true)
    Trainee convertTraineeDtoWithoutTrainersList(TraineeDto traineeDto);
}
