package org.gym.mapper;

import org.gym.dto.TrainingDto;
import org.gym.entity.Training;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TraineeMapper.class, TrainerMapper.class, TrainingTypeMapper.class})
public interface TrainingMapper {
    TrainingDto convertToDto(Training training);
    Training convertToEntity(TrainingDto trainingDto);
}
