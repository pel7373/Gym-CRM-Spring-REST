package org.gym.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.TrainingDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;

    @Override
    public TrainingDto create(TrainingDto trainingDto) throws EntityNotFoundException {
        String trainingTypeName = trainingDto.getTrainingType().getTrainingTypeName();
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName)
                .orElse(null);

        String trainerUserName = trainingDto.getTrainer().getUser().getUserName();
        Trainer trainer = trainerRepository.findByUserName(trainerUserName)
                .orElse(null);

        String traineeUserName = trainingDto.getTrainee().getUser().getUserName();
        Trainee trainee = traineeRepository.findByUserName(traineeUserName)
                .orElse(null);

        Training training = trainingMapper.convertToEntity(trainingDto);

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);

        Training createdTraining = trainingRepository.save(training);
        return trainingMapper.convertToDto(createdTraining);
    }

    @Override
    public List<TrainingDto> getTraineeTrainingsListCriteria(TraineeTrainingsDto traineeTrainingsDto) {

        traineeRepository.findByUserName(traineeTrainingsDto.getTraineeUserName())
                .orElseThrow(() -> new org.gym.exception.EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, traineeTrainingsDto.getTraineeUserName()))
        );

        return trainingRepository.getByTraineeCriteria(traineeTrainingsDto)
                .stream()
                .map(trainingMapper::convertToDto)
                .toList();
    }

    @Override
    public List<TrainingDto> getTrainerTrainingsListCriteria(TrainerTrainingsDto trainerTrainingsDto) {
        trainerRepository.findByUserName(trainerTrainingsDto.getTrainerUserName())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, trainerTrainingsDto.getTrainerUserName()))
        );

        return trainingRepository.getByTrainerCriteria(trainerTrainingsDto)
                .stream()
                .map(trainingMapper::convertToDto)
                .toList();
    }
}
