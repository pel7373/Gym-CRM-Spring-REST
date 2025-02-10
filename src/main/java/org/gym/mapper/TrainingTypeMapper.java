package org.gym.mapper;

import org.gym.dto.TrainingTypeDto;
import org.gym.dto.TrainingTypeResponse;
import org.gym.entity.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {
    TrainingTypeDto convertToDto(TrainingType trainingType);
    TrainingType convertToEntity(TrainingTypeDto trainingTypeDto);
    @Mappings({
            @Mapping(target="trainingTypeName", source="trainingType.trainingTypeName"),
            @Mapping(target="id", source="trainingType.id")
    })
    TrainingTypeResponse convertToTrainingTypeResponse(TrainingType trainingType);
}
