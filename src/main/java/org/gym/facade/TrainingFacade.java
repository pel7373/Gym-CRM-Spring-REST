package org.gym.facade;

import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.TrainingDto;

import java.util.List;

public interface TrainingFacade {
    TrainingDto create(TrainingDto trainingDto);
    List<TrainingDto> getTraineeTrainings(TraineeTrainingsDto traineeTrainingsDto);
    List<TrainingDto> getTrainerTrainings(TrainerTrainingsDto trainerTrainingsDto);
}
