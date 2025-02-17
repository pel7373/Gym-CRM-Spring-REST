package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TraineeDto;
import org.gym.dto.UserDto;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.TrainerDto;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.EntityNotValidException;
import org.gym.exception.NullEntityException;
import org.gym.facade.TrainerFacade;
import org.gym.mapper.TrainerMapper;
import org.gym.service.TrainerService;
import org.gym.validator.JakartaValidator;
import org.gym.validator.UserDtoValidator;
import org.springframework.stereotype.Component;

import static org.gym.config.Config.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainerFacadeImpl implements TrainerFacade {

    private final TrainerService trainerService;
    private final UserDtoValidator userDtoValidator;
    private final TrainerMapper trainerMapper;
    private final UserNameAndPasswordChecker userNameAndPasswordChecker;

    @Override
    public CreateResponse create(TrainerDto trainerDto) throws NullEntityException, EntityNotValidException {
        if(trainerDto == null || trainerDto.getUser() == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
        return trainerMapper.convertToCreateResponse(createdTrainerDto);
    }

    @Override
    public TrainerSelectResponse select(String userName) throws EntityNotFoundException, NullEntityException {
        if(userName == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            TrainerDto selectedTrainerDto = trainerService.select(userName);
            return trainerMapper.convertToTrainerSelectResponse(selectedTrainerDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, userName));
        }
    }

    @Override
    public TrainerUpdateResponse update(String userName, TrainerUpdateRequest trainerUpdateRequest) throws EntityNotFoundException, NullEntityException, EntityNotValidException {
        if(trainerUpdateRequest == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        TrainerDto trainerDto = trainerMapper.convertTrainerUpdateRequestToTrainerDto(trainerUpdateRequest);

        try {
            TrainerDto updatedTrainerDto = trainerService.update(userName, trainerDto);
            TrainerUpdateResponse trainerUpdateResponse = trainerMapper.convertDtoToUpdateResponse(updatedTrainerDto);
            LOGGER.info("Trainer with userName {} was updated", trainerUpdateResponse.getUser().getUserName());
            return trainerUpdateResponse;
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, userName));
        }
    }

    @Override
    public void changeStatus(String userName, Boolean isActive) throws EntityNotFoundException, NullEntityException {
        if(isActive == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            trainerService.changeStatus(userName, isActive);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION);
        }

    }

    @Override
    public boolean authenticate(String userName, String password) {
        if (userNameAndPasswordChecker.isNullOrBlank(userName, password)) {
            LOGGER.warn(USERNAME_PASSWORD_CANT_BE_NULL_OR_BLANK);
            return false;
        }

        return trainerService.authenticate(userName, password);
    }

    @Override
    public TrainerDto changeSpecialization(String userName, String password, TrainingType trainingType) {
        if(trainingType == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            return trainerService.changeSpecialization(userName, trainingType);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            return null;
        }
    }
    
    @Override
    public void changePassword(ChangeLoginRequest changeLoginRequest) throws EntityNotFoundException, NullEntityException {
        if(changeLoginRequest != null || userNameAndPasswordChecker.isNullOrBlank(changeLoginRequest.getNewPassword())) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            trainerService.changePassword(changeLoginRequest.getUserName(), changeLoginRequest.getNewPassword());
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, changeLoginRequest.getUserName());
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, changeLoginRequest.getUserName()));
        }
    }
}
