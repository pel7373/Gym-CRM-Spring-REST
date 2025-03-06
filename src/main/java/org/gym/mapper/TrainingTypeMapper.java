package org.gym.mapper;

import org.gym.dto.TrainingTypeDto;
import org.gym.entity.TrainingType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {
    TrainingTypeDto convertToDto(TrainingType trainingType);
    TrainingType convertToEntity(TrainingTypeDto trainingTypeDto);

//    //@Mapping(target = "specialization", expression="java(trainer.getSpecialization().getTrainingTypeName())")
//    default String convertToString(TrainingType trainingType) {
//        return trainingType.getTrainingTypeName();
//    }
}
