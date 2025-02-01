package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.TrainingDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.facade.TrainingFacade;
import org.gym.service.TrainingService;
import org.gym.validator.TrainingDtoValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.gym.config.Config.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingFacadeImpl implements TrainingFacade {

    private final TrainingService trainingService;
    private final TrainingDtoValidator trainingDtoValidator;
    private final UserNameAndPasswordChecker userNameAndPasswordChecker;

    @Override
    public TrainingDto create(TrainingDto trainingDto) {
        if(trainingDto == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            return null;
        }

        if(!trainingDtoValidator.validate(trainingDto)) {
            LOGGER.warn(trainingDtoValidator.getErrorMessage(trainingDto));
            return null;
        }

        try {
            return trainingService.create(trainingDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public List<TrainingDto> getTraineeTrainings(TraineeTrainingsDto traineeTrainingsDto) {
        if (userNameAndPasswordChecker.isNullOrBlank(traineeTrainingsDto.getTraineeUserName())) {
            LOGGER.warn(USERNAME_CANT_BE_NULL_OR_BLANK);
            return new ArrayList<>();
        }

        try {
            return trainingService.getTraineeTrainingsListCriteria(traineeTrainingsDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, traineeTrainingsDto.getTraineeUserName());
            return new ArrayList<>();
        }
    }

    @Override
    public List<TrainingDto> getTrainerTrainings(TrainerTrainingsDto trainerTrainingsDto) {
        if(userNameAndPasswordChecker.isNullOrBlank(trainerTrainingsDto.getTrainerUserName())) {
            LOGGER.warn(USERNAME_CANT_BE_NULL_OR_BLANK);
            return new ArrayList<>();
        }

        try {
            return trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, trainerTrainingsDto.getTrainerUserName());
            return new ArrayList<>();
        }
    }
}
