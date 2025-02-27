package org.gym.service;

import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.TrainingDto;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;
import org.gym.exception.EntityNotFoundException;

import java.util.List;

public interface TrainingService {
    void create(TrainingAddRequest requesto) throws EntityNotFoundException;
    List<TraineeTrainingsListResponse> getTraineeTrainingsListCriteria(TraineeTrainingsDto traineeTrainingsDto);
    List<TrainerTrainingsListResponse> getTrainerTrainingsListCriteria(TrainerTrainingsDto trainerTrainingsDto);
}
