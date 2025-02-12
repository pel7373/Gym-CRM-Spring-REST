package org.gym.facade.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.*;
import org.gym.exception.EntityNotValidException;
import org.gym.exception.NullEntityException;
import org.gym.mapper.TraineeMapper;
import org.gym.validator.JakartaValidator;
import org.gym.validator.UserDtoValidator;
import org.gym.exception.EntityNotFoundException;
import org.gym.facade.TraineeFacade;
import org.gym.service.TraineeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.gym.config.Config.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeFacadeImpl implements TraineeFacade {

    private final TraineeService traineeService;
    private final UserNameAndPasswordChecker userNameAndPasswordChecker;
    private final TraineeMapper traineeMapper;
    private final JakartaValidator<TraineeDto> validatorTraineeDto;
    private final JakartaValidator<UserDto> validatorUserDto;

    @Override
    public TraineeCreateResponse create(TraineeDto traineeDto) {
        if(traineeDto == null || traineeDto.getUser() == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        if(!validatorTraineeDto.validate(traineeDto) || !validatorUserDto.validate(traineeDto.getUser())) {
            String message = validatorTraineeDto.getErrors(traineeDto) + validatorUserDto.getErrors(traineeDto.getUser());
            LOGGER.warn("Invalid entity {}: {}", traineeDto, message);
            throw new EntityNotValidException(message);
        }

        TraineeDto createdTraineeDto = traineeService.create(traineeDto);
        return TraineeCreateResponse
                .builder()
                .userName(createdTraineeDto.getUser().getUserName())
                .password(createdTraineeDto.getUser().getPassword())
                .build();
    }

    @Override
    public TraineeProfileResponse select(String userName) {
        try {
            TraineeDto selectedTraineeDto = traineeService.select(userName);
            return traineeMapper.convertToTraineeProfileResponse(selectedTraineeDto);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public TraineeDto update(String userName,TraineeDto traineeDto) {
//        if(traineeDto == null) {
//            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
//            return null;
//        }

//        if(!userDtoValidator.validate(traineeDto.getUser())) {
//            LOGGER.warn(userDtoValidator.getErrorMessage(traineeDto.getUser()));
//            return null;
//        }

//        if(!authenticate(userName, password)) {
//            LOGGER.warn(ACCESS_DENIED, userName);
//            return null;
//        }

        try {
            return traineeService.update(userName, traineeDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            return null;
        }
    }

    @Override
    public void delete(String userName) {
        //if(authenticate(userName, password)) {
            traineeService.delete(userName);
//        } else {
//            LOGGER.warn(ACCESS_DENIED, userName);
//        }
    }

    @Override
    public TraineeDto changeStatus(String userName, Boolean isActive) {
        if(isActive == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            return null;
        }

        //if(authenticate(userName, password)) {
            try {
                return traineeService.changeStatus(userName, isActive);
            } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            return null;
            }
//        } else {
//            LOGGER.warn(ACCESS_DENIED, userName);
//            return null;
//        }
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
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            return null;
        }

        //if(authenticate(userName, password)) {
            try {
                return traineeService.changePassword(userName, newPassword);
            } catch (EntityNotFoundException e) {
                LOGGER.warn(ENTITY_NOT_FOUND, userName);
                return null;
            }
//        } else {
//                LOGGER.warn(ACCESS_DENIED, userName);
//                return null;
//            }
    }

    @Override
    public List<TrainerDto> getUnassignedTrainers(String userName) {
        //if(authenticate(userName, password)) {
            try {
                return traineeService.getUnassignedTrainersList(userName);
            } catch (EntityNotFoundException e) {
                LOGGER.warn(ENTITY_NOT_FOUND, userName);
                return new ArrayList<>();
            }
//        } else {
//        LOGGER.warn(ACCESS_DENIED, userName);
//        return new ArrayList<>();
//        }
    }

    @Override
    public List<TrainerDto> updateTrainersList(String userName, List<String> trainersUserNames) {
        if(trainersUserNames == null || trainersUserNames.isEmpty()) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            return new ArrayList<>();
        }

        //if(authenticate(userName, password)) {
            try {
                return traineeService.updateTrainersList(userName, trainersUserNames);
            } catch (EntityNotFoundException e) {
                LOGGER.warn(ENTITY_NOT_FOUND, userName);
                return new ArrayList<>();
            }
//        } else {
//            LOGGER.warn(ACCESS_DENIED, userName);
//            return new ArrayList<>();
//        }
    }
}
