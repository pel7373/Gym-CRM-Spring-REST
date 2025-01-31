package org.gym.service;

import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.TrainingDto;
import org.gym.exception.EntityNotFoundException;

import java.util.List;

public interface TrainingService {
    TrainingDto create(TrainingDto trainingDto) throws EntityNotFoundException;
    List<TrainingDto> getTraineeTrainingsListCriteria(TraineeTrainingsDto traineeTrainingsDto);
    List<TrainingDto> getTrainerTrainingsListCriteria(TrainerTrainingsDto trainerTrainingsDto);
}
