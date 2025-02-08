package org.gym.facade;

import org.gym.dto.TraineeCreateResponse;
import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;

import java.util.List;

public interface TraineeFacade {
    TraineeCreateResponse create(TraineeDto traineeDto);
    TraineeDto select(String userName);
    TraineeDto update(String userName, TraineeDto traineeDto);
    TraineeDto changeStatus(String userName, Boolean isActive);
    boolean authenticate(String userName, String password);
    TraineeDto changePassword(String userName, String password, String newPassword);
    void delete(String userName);
    List<TrainerDto> getUnassignedTrainers(String userName);
    List<TrainerDto> updateTrainersList(String userName, List<String> trainersUserNames);
}
