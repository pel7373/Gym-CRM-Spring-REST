package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.TrainerDto;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.NullEntityException;
import org.gym.facade.TrainerFacade;
import org.gym.mapper.TrainerMapper;
import org.gym.service.TrainerService;
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
    public CreateResponse create(TrainerDto trainerDto) {
        if(trainerDto == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

//        if(trainerDto.getUser().getIsActive() == null) {
//            trainerDto.getUser().setIsActive(true);
//        }

//        if(!userDtoValidator.validate(trainerDto.getUser())) {
//            LOGGER.warn(userDtoValidator.getErrorMessage(trainerDto.getUser()));
//            return null;
//        }

//        try {
//            return trainerService.create(trainerDto);
//        } catch (EntityNotFoundException e) {
//            LOGGER.warn(ENTITY_NOT_FOUND, trainerDto.getUser().getFirstName());
//            return null;
//        }
        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
        return trainerMapper.convertToCreateResponse(createdTrainerDto);
    }

    @Override
    public TrainerDto select(String userName, String password) {
        if(authenticate(userName, password)) {
            try {
                return trainerService.select(userName);
            } catch (EntityNotFoundException e) {
                LOGGER.warn(ENTITY_NOT_FOUND, userName);
            }
        } else {
            LOGGER.warn(ACCESS_DENIED, userName);
        }
        return null;
    }

    @Override
    public TrainerDto update(String userName, String password, TrainerDto trainerDto) {
        if(trainerDto == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            return null;
        }

        if(!userDtoValidator.validate(trainerDto.getUser())) {
            LOGGER.warn(userDtoValidator.getErrorMessage(trainerDto.getUser()));
            return null;
        }

        if(!authenticate(userName, password)) {
            LOGGER.warn(ACCESS_DENIED, userName);
            return null;
        }

        try {
            return trainerService.update(userName, trainerDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            return null;
        }
    }

    @Override
    public TrainerDto changeStatus(String userName, String password, Boolean isActive) {
        if(isActive == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            return null;
        }

        if(authenticate(userName, password)) {
            try {
                return trainerService.changeStatus(userName, isActive);
            } catch (EntityNotFoundException e) {
                LOGGER.warn(ENTITY_NOT_FOUND, userName);
                return null;
            }
        } else {
            LOGGER.warn(ACCESS_DENIED, userName);
            return null;
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
            return null;
        }

        if(authenticate(userName, password)) {
            try {
                return trainerService.changeSpecialization(userName, trainingType);
            } catch (EntityNotFoundException e) {
                LOGGER.warn(ENTITY_NOT_FOUND, userName);
                return null;
            }
        } else {
            LOGGER.warn(ACCESS_DENIED, userName);
            return null;
        }
    }
    
    @Override
    public void changePassword(ChangeLoginRequest changeLoginRequest) {
        if(changeLoginRequest != null || userNameAndPasswordChecker.isNullOrBlank(changeLoginRequest.getNewPassword())) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

            try {
                trainerService.changePassword(changeLoginRequest.getUserName(), changeLoginRequest.getNewPassword());
            } catch (EntityNotFoundException e) {
                LOGGER.warn(ENTITY_NOT_FOUND, changeLoginRequest.getUserName());
                throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION);
            }
    }
}
