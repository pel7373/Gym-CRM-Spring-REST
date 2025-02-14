package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.*;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.exception.EntityNotValidException;
import org.gym.exception.NullEntityException;
import org.gym.mapper.TraineeMapper;
import org.gym.validator.JakartaValidator;
import org.gym.exception.EntityNotFoundException;
import org.gym.facade.TraineeFacade;
import org.gym.service.TraineeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public CreateResponse create(TraineeDto traineeDto) {
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
        return traineeMapper.convertToCreateResponse(createdTraineeDto);
    }

    @Override
    public TraineeSelectResponse select(String userName) {
        try {
            TraineeDto selectedTraineeDto = traineeService.select(userName);
            return traineeMapper.convertToTraineeSelectResponse(selectedTraineeDto);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, userName));
        }
    }

    @Override
    public TraineeUpdateResponse update(String userName, TraineeUpdateRequest traineeUpdateRequest) {
        if(traineeUpdateRequest == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        TraineeDto traineeDto = traineeMapper.convertTraineeUpdateRequestToTraineeDto(traineeUpdateRequest);
        LOGGER.info("{}", traineeUpdateRequest);
        LOGGER.info("{}", traineeDto);
        if(!validatorTraineeDto.validate(traineeDto) || !validatorUserDto.validate(traineeDto.getUser())) {
            String message = validatorTraineeDto.getErrors(traineeDto) + validatorUserDto.getErrors(traineeDto.getUser());
            LOGGER.warn("Invalid entity {}: {}", traineeDto, message);
            throw new EntityNotValidException(message);
        }

        try {
            TraineeDto updatedTraineeDto = traineeService.update(userName, traineeDto);
            LOGGER.info("{}", updatedTraineeDto);
            TraineeUpdateResponse traineeUpdateResponse = traineeMapper.convertDtoToUpdateResponse(updatedTraineeDto);
            LOGGER.info("{}", traineeUpdateResponse);
            return traineeUpdateResponse;
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            return null;
        }
    }

    @Override
    public void delete(String userName) {
        traineeService.delete(userName);
    }

    @Override
    public TraineeDto changeStatus(String userName, Boolean isActive) {
        if(isActive == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            return traineeService.changeStatus(userName, isActive);
        } catch (EntityNotFoundException e) {
        LOGGER.warn(ENTITY_NOT_FOUND, userName);
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION);
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
    public void changePassword(ChangeLoginRequest changeLoginRequest) {
        if(changeLoginRequest != null || userNameAndPasswordChecker.isNullOrBlank(changeLoginRequest.getNewPassword())) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            traineeService.changePassword(changeLoginRequest.getUserName(), changeLoginRequest.getNewPassword());
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, changeLoginRequest.getUserName());
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION);
        }
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
