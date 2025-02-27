package org.gym.mapper;

import org.gym.dto.TrainingDto;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;
import org.gym.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TraineeMapper.class, TrainerMapper.class, TrainingTypeMapper.class})
public interface TrainingMapper {
    @Mapping(source = "trainee", target = "trainee", qualifiedByName = "convertToDto")
    @Mapping(source = "trainer", target = "trainer", qualifiedByName = "convertToDto")
    TrainingDto convertToDto(Training training);

    @Mapping(source = "trainee", target = "trainee", qualifiedByName = "convertToEntity")
    @Mapping(source = "trainer", target = "trainer", qualifiedByName = "convertToEntity")
    Training convertToEntity(TrainingDto trainingDto);

//    @Mapping(target = "trainee.user.userName", source = "traineeUserName")
//    @Mapping(target = "trainer.user.userName", source = "trainerUserName")
//    TrainingDto trainingAddRequestToTrainingDto(TrainingAddRequest request);

    @Mapping(target = "trainerUserName", source = "trainer.user.userName")
    TraineeTrainingsListResponse trainingDtoToTraineeTrainingsListResponse(TrainingDto trainingDto);

    @Mapping(target = "traineeUserName", source = "trainee.user.userName")
    TrainerTrainingsListResponse trainingDtoToTrainerTrainingsListResponse(TrainingDto trainingDto);
}
