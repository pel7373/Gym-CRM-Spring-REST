package org.gym.facade;

import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.TrainerDto;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.EntityNotValidException;
import org.gym.exception.NullEntityException;

public interface TrainerFacade {
    CreateResponse create(TrainerDto trainerDto) throws NullEntityException, EntityNotValidException;
    TrainerSelectResponse select(String userName) throws EntityNotFoundException, NullEntityException;
    TrainerUpdateResponse update(String userName, TrainerUpdateRequest trainerUpdateRequest) throws EntityNotFoundException, NullEntityException, EntityNotValidException;
    void changeStatus(String userName, Boolean isActive) throws EntityNotFoundException, NullEntityException;
    boolean authenticate(String userName, String password);
    void changePassword(ChangeLoginRequest changeLoginRequest) throws EntityNotFoundException, NullEntityException;
    TrainerDto changeSpecialization(String userName, String password, TrainingType trainingType);
}
