package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.*;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.exception.EntityNotValidException;
import org.gym.exception.NullEntityException;
import org.gym.mapper.TraineeMapper;
import org.gym.mapper.TrainerMapper;
import org.gym.mapper.TrainerMapperImpl;
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
    private final TrainerMapper trainerMapper;

    @Override
    public CreateResponse create(TraineeDto traineeDto) throws NullEntityException, EntityNotValidException {
        if(traineeDto == null || traineeDto.getUser() == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        TraineeDto createdTraineeDto = traineeService.create(traineeDto);
        return traineeMapper.convertToCreateResponse(createdTraineeDto);
    }

    @Override
    public TraineeSelectResponse select(String userName) throws EntityNotFoundException, NullEntityException {
        if(userName == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            TraineeDto selectedTraineeDto = traineeService.select(userName);
            return traineeMapper.convertToTraineeSelectResponse(selectedTraineeDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, userName));
        }
    }

    @Override
    public TraineeUpdateResponse update(String userName, TraineeUpdateRequest traineeUpdateRequest) throws EntityNotFoundException, NullEntityException, EntityNotValidException {
        if(traineeUpdateRequest == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        TraineeDto traineeDto = traineeMapper.convertTraineeUpdateRequestToTraineeDto(traineeUpdateRequest);

        try {
            TraineeDto updatedTraineeDto = traineeService.update(userName, traineeDto);
            TraineeUpdateResponse traineeUpdateResponse = traineeMapper.convertDtoToUpdateResponse(updatedTraineeDto);
            LOGGER.info("{}", traineeUpdateResponse);
            return traineeUpdateResponse;
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, userName));
        }
    }

    @Override
    public void delete(String userName) throws EntityNotFoundException,  NullEntityException {
        if(userName == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            traineeService.delete(userName);
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
            traineeService.changeStatus(userName, isActive);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, userName));
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
    public void changePassword(ChangeLoginRequest changeLoginRequest) throws EntityNotFoundException, NullEntityException {
        if(changeLoginRequest == null
                || userNameAndPasswordChecker.isNullOrBlank(changeLoginRequest.getUserName(), changeLoginRequest.getOldPassword())
                || userNameAndPasswordChecker.isNullOrBlank(changeLoginRequest.getNewPassword())) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            traineeService.changePassword(changeLoginRequest.getUserName(), changeLoginRequest.getNewPassword());
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, changeLoginRequest.getUserName());
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, changeLoginRequest.getUserName()));
        }
    }

    @Override
    public List<TrainerForListResponse> getUnassignedTrainers(String userName) throws EntityNotFoundException {
        try {
            List<TrainerDto> unassignedTrainersDtoList = traineeService.getUnassignedTrainersList(userName);
            return trainerMapper.convertTrainerDtoListToTrainerResponseList(unassignedTrainersDtoList);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, userName));
        }
    }

    @Override
    public List<TrainerForListResponse> updateTrainersList(String userName, List<String> trainersUserNames) {
        if(trainersUserNames == null || trainersUserNames.isEmpty()) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            return new ArrayList<>();
        }

        try {
            return trainerMapper.convertTrainerDtoListToTrainerResponseList(traineeService.updateTrainersList(userName, trainersUserNames));
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, userName);
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_EXCEPTION, userName));
        }
    }
}
