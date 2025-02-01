package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TrainerDto;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;
import org.gym.facade.TrainerFacade;
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
    private final UserNameAndPasswordChecker userNameAndPasswordChecker;

    @Override
    public TrainerDto create(TrainerDto trainerDto) {
        if(trainerDto == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            return null;
        }

        if(!userDtoValidator.validate(trainerDto.getUser())) {
            LOGGER.warn(userDtoValidator.getErrorMessage(trainerDto.getUser()));
            return null;
        }

        try {
            return trainerService.create(trainerDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, trainerDto.getUser().getFirstName());
            return null;
        }
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
    public TrainerDto changePassword(String userName, String password, String newPassword) {
        if(userNameAndPasswordChecker.isNullOrBlank(newPassword)) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            return null;
        }

        if(authenticate(userName, password)) {
            try {
                return trainerService.changePassword(userName, newPassword);
            } catch (EntityNotFoundException e) {
                LOGGER.warn(ENTITY_NOT_FOUND, userName);
                return null;
            }
        } else {
            LOGGER.warn(ACCESS_DENIED, userName);
            return null;
        }
    }
}
