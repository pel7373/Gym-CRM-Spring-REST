package org.gym.facade;

import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.TrainingDto;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;

import java.util.List;

public interface TrainingFacade {
    void create(TrainingAddRequest request);
    List<TraineeTrainingsListResponse> getTraineeTrainings(TraineeTrainingsDto traineeTrainingsDto);
    List<TrainerTrainingsListResponse> getTrainerTrainings(TrainerTrainingsDto trainerTrainingsDto);
}
