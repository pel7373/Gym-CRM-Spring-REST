package org.gym.service;

import org.gym.dto.TrainerDto;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;

public interface TrainerService {
    CreateResponse create(TrainerDto trainerDto);
    TrainerSelectResponse select(String userName) throws EntityNotFoundException;
    TrainerUpdateResponse update(String userName, TrainerUpdateRequest trainerUpdateRequest) throws EntityNotFoundException;
    TrainerDto changeSpecialization(String userName, TrainingType trainingType) throws EntityNotFoundException;
}
