package org.gym.facade;

import org.gym.dto.*;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;

import java.util.List;

public interface TraineeFacade {
    CreateResponse create(TraineeDto traineeDto);
    TraineeSelectResponse select(String userName);
    TraineeUpdateResponse update(String userName, TraineeUpdateRequest traineeUpdateRequest);
    TraineeDto changeStatus(String userName, Boolean isActive);
    boolean authenticate(String userName, String password);
    void changePassword(ChangeLoginRequest changeLoginRequest);
    void delete(String userName);
    List<TrainerDto> getUnassignedTrainers(String userName);
    List<TrainerDto> updateTrainersList(String userName, List<String> trainersUserNames);
}
