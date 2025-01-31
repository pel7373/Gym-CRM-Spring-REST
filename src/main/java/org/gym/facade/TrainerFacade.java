package org.gym.facade;

import org.gym.dto.TrainerDto;
import org.gym.entity.TrainingType;

import java.util.List;

public interface TrainerFacade {
    TrainerDto create(TrainerDto trainerDto);
    TrainerDto select(String userName, String password);
    TrainerDto update(String userName, String password, TrainerDto trainerDto);
    TrainerDto changeStatus(String userName, String password, Boolean isActive);
    boolean authenticate(String userName, String password);
    TrainerDto changePassword(String userName, String password, String newPassword);
    TrainerDto changeSpecialization(String userName, String password, TrainingType trainingType);
    List<TrainerDto> getUnassignedTrainers(String traineeUserName);
    List<TrainerDto> updateTrainersList(String traineeUserName, List<String> trainersUserNames);
}
