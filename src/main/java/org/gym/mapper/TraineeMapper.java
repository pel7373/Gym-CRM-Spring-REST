package org.gym.mapper;

import org.gym.dto.TraineeCreateRequest;
import org.gym.dto.TraineeCreateResponse;
import org.gym.dto.TraineeDto;
import org.gym.entity.Trainee;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = UserMapper.class)
@Component
public interface TraineeMapper {
    TraineeDto convertToDto(Trainee trainee);
    Trainee convertToEntity(TraineeDto traineeDto);
    TraineeCreateResponse convertToTraineeCreateResponse(Trainee trainee);
    TraineeDto convertCreateRequestToDto(TraineeCreateRequest trainee);
}
