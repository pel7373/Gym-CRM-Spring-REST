package org.gym.service;

import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;
import org.gym.exception.EntityNotFoundException;

import java.util.List;

public interface TraineeService {
    TraineeDto create(TraineeDto traineeDto);
    TraineeDto select(String userName) throws EntityNotFoundException;
    TraineeDto update(String userName, TraineeDto traineeDto) throws EntityNotFoundException;
    TraineeDto changeStatus(String userName, Boolean isActive) throws EntityNotFoundException;
    boolean authenticate(String userName, String password);
    TraineeDto changePassword(String userName, String newPassword) throws EntityNotFoundException;
    void delete(String userName);
    List<TrainerDto> getUnassignedTrainersList(String username) throws EntityNotFoundException;
    List<TrainerDto> updateTrainersList(String username, List<String> trainersUserNames) throws EntityNotFoundException;
}
