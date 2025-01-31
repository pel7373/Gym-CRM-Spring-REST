package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TraineeDto;
import org.gym.validator.UserDtoValidator;
import org.gym.exception.EntityNotFoundException;
import org.gym.facade.TraineeFacade;
import org.gym.service.TraineeService;
import org.springframework.stereotype.Component;

import static org.gym.config.Config.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TraineeFacadeImpl implements TraineeFacade {

    private final TraineeService traineeService;
    private final UserDtoValidator userDtoValidator;
    private final UserNameAndPasswordChecker userNameAndPasswordChecker;

    @Override
    public TraineeDto create(TraineeDto traineeDto) {
        if(traineeDto == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL);
            return null;
        }

        if(!userDtoValidator.validate(traineeDto.getUser())) {
            LOGGER.warn(userDtoValidator.getErrorMessage(traineeDto.getUser()));
            return null;
        }

        TraineeDto traineeDtoResult = null;
        try {
            traineeDtoResult = traineeService.create(traineeDto);
            return traineeDtoResult;
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, traineeDto);
        }
        return traineeDtoResult;
    }

    @Override
    public TraineeDto select(String userName, String password) {
        if(authenticate(userName, password)) {
            try {
                return traineeService.select(userName);
            } catch (EntityNotFoundException e) {
                LOGGER.warn(ENTITY_NOT_FOUND, userName);
            }
        } else {
            LOGGER.warn(ACCESS_DENIED, userName);
        }
        return null;
    }

    @Override
    public TraineeDto update(String userName, String password, TraineeDto traineeDto) {
        if(traineeDto == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL);
            return null;
        }

        if(!userDtoValidator.validate(traineeDto.getUser())) {
            LOGGER.warn(userDtoValidator.getErrorMessage(traineeDto.getUser()));
            return null;
        }

        if(!authenticate(userName, password)) {
            LOGGER.warn(ACCESS_DENIED, userName);
            return null;
        }

        try {
            return traineeService.update(userName, traineeDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            return null;
        }
    }

    @Override
    public void delete(String userName, String password) {
        if(authenticate(userName, password)) {
            traineeService.delete(userName);
        } else {
            LOGGER.warn(ACCESS_DENIED, userName);
        }
    }

    @Override
    public TraineeDto changeStatus(String userName, String password, Boolean isActive) {
        if(isActive == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL);
            return null;
        }

        if(authenticate(userName, password)) {
            try {
                return traineeService.changeStatus(userName, isActive);
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
        if(userNameAndPasswordChecker.isNullOrBlank(userName, password)) {
            LOGGER.warn(USERNAME_PASSWORD_CANT_BE_NULL_OR_BLANK);
            return false;
        }
        return traineeService.authenticate(userName, password);
    }

    @Override
    public TraineeDto changePassword(String userName, String password, String newPassword) {
        if(userNameAndPasswordChecker.isNullOrBlank(newPassword)) {
            LOGGER.warn("new password can't be null or blank");
            return null;
        }

        if(authenticate(userName, password)) {
            try {
                return traineeService.changePassword(userName, newPassword);
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
