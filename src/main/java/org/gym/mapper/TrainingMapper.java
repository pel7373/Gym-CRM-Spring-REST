package org.gym.mapper;

import org.gym.dto.TrainingDto;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;
import org.gym.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TraineeMapper.class, TrainerMapper.class, TrainingTypeMapper.class})
public interface TrainingMapper {
//    @Mapping(source = "trainee", target = "trainee")
//    @Mapping(source = "trainer", target = "trainer")
    TrainingDto convertToDto(Training training);

//    @Mapping(source = "trainee", target = "trainee")
//    @Mapping(source = "trainer", target = "trainer")
    Training convertToEntity(TrainingDto trainingDto);

    @Mapping(target = "trainerUserName", expression = "java(training.getTrainer().getUser().getUserName())")
    @Mapping(target = "trainingType", expression = "java(training.getTrainingType().getTrainingTypeName())")
    TraineeTrainingsListResponse trainingToTraineeTrainingsListResponse(Training training);

    @Mapping(target = "traineeUserName", expression = "java(training.getTrainee().getUser().getUserName())")
    @Mapping(target = "trainingType", expression = "java(training.getTrainingType().getTrainingTypeName())")
    TrainerTrainingsListResponse trainingToTrainerTrainingsListResponse(Training training);
}
