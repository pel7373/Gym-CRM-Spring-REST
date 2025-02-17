package org.gym.facade;

import org.gym.dto.*;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.EntityNotValidException;
import org.gym.exception.NullEntityException;

import java.util.List;

public interface TraineeFacade {
    CreateResponse create(TraineeDto traineeDto) throws NullEntityException, EntityNotValidException;
    TraineeSelectResponse select(String userName) throws EntityNotFoundException, NullEntityException;
    TraineeUpdateResponse update(String userName, TraineeUpdateRequest traineeUpdateRequest) throws EntityNotFoundException, NullEntityException, EntityNotValidException;
    void changeStatus(String userName, Boolean isActive) throws EntityNotFoundException, NullEntityException;
    boolean authenticate(String userName, String password);
    void changePassword(ChangeLoginRequest changeLoginRequest) throws EntityNotFoundException, NullEntityException;
    void delete(String userName) throws EntityNotFoundException, NullEntityException;
    List<TrainerForListResponse> getUnassignedTrainers(String userName) throws EntityNotFoundException;
    List<TrainerForListResponse> updateTrainersList(String userName, List<String> trainersUserNames);
}
