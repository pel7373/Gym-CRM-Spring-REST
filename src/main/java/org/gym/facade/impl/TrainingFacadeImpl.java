package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.*;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.NullEntityException;
import org.gym.facade.TrainingFacade;
import org.gym.mapper.TrainingMapper;
import org.gym.service.TrainingService;
import org.gym.validator.JakartaValidator;
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
    private final UserNameAndPasswordChecker userNameAndPasswordChecker;
    private final TrainingMapper trainingMapper;

    @Override
    public void create(TrainingAddRequest request) {
        if(request == null) {
            LOGGER.warn(ENTITY_CANT_BE_NULL_OR_BLANK);
            throw new NullEntityException(ENTITY_CANT_BE_NULL_OR_BLANK);
        }

        try {
            TrainingDto trainingDto = trainingMapper.trainingAddRequestToTrainingDto(request);
            trainingService.create(trainingDto);
        } catch (EntityNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public List<TraineeTrainingsListResponse> getTraineeTrainings(TraineeTrainingsDto traineeTrainingsDto) {

        try {
            //return
            List<TrainingDto> traineeTrainingsList = trainingService.getTraineeTrainingsListCriteria(traineeTrainingsDto);
            return traineeTrainingsList.stream()
                    .map(trainingMapper::trainingDtoToTraineeTrainingsListResponse)
                    .toList();
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, traineeTrainingsDto.getTraineeUserName());
            return new ArrayList<>();
        }
    }

    @Override
    public List<TrainerTrainingsListResponse> getTrainerTrainings(TrainerTrainingsDto trainerTrainingsDto) {

        try {
            List<TrainingDto> trainerTrainingsList = trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto);
            return trainerTrainingsList.stream()
                    .map(trainingMapper::trainingDtoToTrainerTrainingsListResponse)
                    .toList();
        } catch (EntityNotFoundException e) {
            LOGGER.warn(ENTITY_NOT_FOUND, trainerTrainingsDto.getTrainerUserName());
            return new ArrayList<>();
        }
    }
}
