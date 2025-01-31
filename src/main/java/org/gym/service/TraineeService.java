package org.gym.service;

import org.gym.dto.TraineeDto;
import org.gym.exception.EntityNotFoundException;

public interface TraineeService {
    TraineeDto create(TraineeDto traineeDto);
    TraineeDto select(String userName) throws EntityNotFoundException;
    TraineeDto update(String userName, TraineeDto traineeDto) throws EntityNotFoundException;
    TraineeDto changeStatus(String userName, Boolean isActive) throws EntityNotFoundException;
    boolean authenticate(String userName, String password);
    TraineeDto changePassword(String userName, String newPassword) throws EntityNotFoundException;
    void delete(String userName);
}
