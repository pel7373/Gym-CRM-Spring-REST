package org.gym.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.annotation.GymService;
import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.TrainingDto;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.Training;
import org.gym.entity.TrainingType;
import org.gym.mapper.TrainingMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.TrainingService;
import org.gym.exception.EntityNotFoundException;

import java.util.List;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
@GymService
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;

    @Override
    public void create(TrainingAddRequest request) throws EntityNotFoundException {
        String trainingTypeName = request.getTrainingType().getTrainingTypeName();
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, request.getTrainingType())));

        Trainer trainer = trainerRepository.findByUserName(request.getTrainerUserName())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, request.getTrainerUserName())));

        Trainee trainee = traineeRepository.findByUserName(request.getTraineeUserName())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, request.getTraineeUserName())));

        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(request.getTrainingName())
                .trainingType(trainingType)
                .date(request.getDate())
                .duration(request.getDuration())
                .build();

        trainingRepository.save(training);
    }

    @Override
    public List<TraineeTrainingsListResponse> getTraineeTrainingsListCriteria(TraineeTrainingsDto traineeTrainingsDto) {

        List<TrainingDto> trainingDtoList = trainingRepository.getByTraineeCriteria(traineeTrainingsDto)
                .stream()
                .map(trainingMapper::convertToDto)
                .toList();

        List<TraineeTrainingsListResponse> traineeTrainingsListResponseList =
                trainingDtoList.stream()
                        .map(trainingMapper::trainingDtoToTraineeTrainingsListResponse)
                        .toList();
        return traineeTrainingsListResponseList;
    }

    @Override
    public List<TrainerTrainingsListResponse> getTrainerTrainingsListCriteria(TrainerTrainingsDto trainerTrainingsDto) {
        trainerRepository.findByUserName(trainerTrainingsDto.getTrainerUserName())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, trainerTrainingsDto.getTrainerUserName()))
        );

        List<TrainingDto> trainingDtoList = trainingRepository.getByTrainerCriteria(trainerTrainingsDto)
                .stream()
                .map(trainingMapper::convertToDto)
                .toList();

        List<TrainerTrainingsListResponse> trainerTrainingsListResponseList = trainingDtoList.stream()
                .map(trainingMapper::trainingDtoToTrainerTrainingsListResponse)
                .toList();

        return trainerTrainingsListResponseList;
    }
}
