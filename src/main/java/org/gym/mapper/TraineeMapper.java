package org.gym.mapper;

import org.gym.dto.TraineeDto;
import org.gym.entity.Trainee;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = UserMapper.class)
@Component
public interface TraineeMapper {
    TraineeDto convertToDto(Trainee trainee);
    Trainee convertToEntity(TraineeDto traineeDto) ;
}
