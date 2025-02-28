package org.gym.mapper;

import org.gym.dto.TrainingTypeDto;
import org.gym.dto.response.trainingType.TrainingTypeResponse;
import org.gym.entity.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {
    TrainingTypeDto convertToDto(TrainingType trainingType);
    TrainingType convertToEntity(TrainingTypeDto trainingTypeDto);
}
