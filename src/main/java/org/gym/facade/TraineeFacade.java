package org.gym.facade;

import org.gym.dto.TraineeDto;

public interface TraineeFacade {
    TraineeDto create(TraineeDto traineeDto);
    TraineeDto select(String userName, String password);
    TraineeDto update(String userName, String password, TraineeDto traineeDto);
    TraineeDto changeStatus(String userName, String password, Boolean isActive);
    boolean authenticate(String userName, String password);
    TraineeDto changePassword(String userName, String password, String newPassword);
    void delete(String userName, String password);
}
