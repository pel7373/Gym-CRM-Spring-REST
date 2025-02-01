package org.gym.service;

import org.gym.dto.TrainerDto;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;

public interface TrainerService {
    TrainerDto create(TrainerDto trainerDto);
    TrainerDto select(String userName) throws EntityNotFoundException;
    TrainerDto update(String userName, TrainerDto trainerDto) throws EntityNotFoundException;
    TrainerDto changeStatus(String userName, Boolean isActive) throws EntityNotFoundException;
    boolean authenticate(String userName, String password);
    TrainerDto changePassword(String userName, String newPassword) throws EntityNotFoundException;
    TrainerDto changeSpecialization(String userName, TrainingType trainingType) throws EntityNotFoundException;
}
