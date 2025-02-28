package org.gym.service;

import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.exception.EntityNotFoundException;

import java.util.List;

public interface TraineeService {
    CreateResponse create(TraineeDto traineeDto);
    TraineeSelectResponse select(String userName) throws EntityNotFoundException;
    TraineeUpdateResponse update(String userName, TraineeUpdateRequest traineeUpdateRequest) throws EntityNotFoundException;
    void delete(String userName) throws EntityNotFoundException;
    List<TrainerForListResponse> getUnassignedTrainersList(String userName) throws EntityNotFoundException;
    List<TrainerForListResponse> updateTrainersList(String userName, List<String> trainersUserNames) throws EntityNotFoundException;
}
