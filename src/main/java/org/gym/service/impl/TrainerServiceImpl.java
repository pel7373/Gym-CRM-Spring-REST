package org.gym.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TrainerDto;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainerMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.TrainerService;
import org.gym.service.UserNameGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserNameGeneratorService userNameGeneratorService;
    private final PasswordGeneratorService passwordGeneratorService;
    private final TrainerMapper trainerMapper;

    @Override
    public TrainerDto select(String userName) throws EntityNotFoundException {
        return trainerMapper.convertToDto(trainerRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName)))
        );
    }

    @Override
    public TrainerDto create(TrainerDto trainerDto) {
        trainerDto.getUser().setUserName(
                userNameGeneratorService.generate(
                        trainerDto.getUser().getFirstName(),
                        trainerDto.getUser().getLastName()
                ));

        String trainingTypeName = trainerDto.getSpecialization().getTrainingTypeName();
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName)
                .orElse(null);
        Trainer trainer = trainerMapper.convertToEntity(trainerDto);
        trainer.setSpecialization(trainingType);
        trainer.getUser().setPassword(passwordGeneratorService.generate());
        return trainerMapper.convertToDto(trainerRepository.save(trainer));
    }

    @Override
    public TrainerDto update(String userName, TrainerDto trainerDto) throws EntityNotFoundException {
        Trainer oldTrainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
        );
        if (isFirstOrLastNamesChanged(trainerDto, oldTrainer)) {
            oldTrainer.getUser().setUserName(
                    userNameGeneratorService.generate(trainerDto.getUser().getFirstName(),
                            trainerDto.getUser().getLastName()));
            oldTrainer.getUser().setFirstName(trainerDto.getUser().getFirstName());
            oldTrainer.getUser().setLastName(trainerDto.getUser().getLastName());
        }
        oldTrainer.getUser().setIsActive(trainerDto.getUser().getIsActive());
        String trainingTypeName = trainerDto.getSpecialization().getTrainingTypeName();
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName)
                .orElse(null);

        oldTrainer.setSpecialization(trainingType);
        return trainerMapper.convertToDto(trainerRepository.save(oldTrainer));
    }

    @Override
    public TrainerDto changeStatus(String userName, Boolean isActive) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
        );
        trainer.getUser().setIsActive(isActive);
        return trainerMapper.convertToDto(trainerRepository.save(trainer));
    }

    @Override
    public TrainerDto changeSpecialization(String userName, TrainingType trainingType) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName)));
        trainer.setSpecialization(trainingType);
        return trainerMapper.convertToDto(trainerRepository.save(trainer));
    }

    @Override
    public boolean authenticate(String userName, String password) {
        Trainer trainer;
        try {
            trainer = trainerRepository.findByUserName(userName)
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format(ENTITY_NOT_FOUND_EXCEPTION, userName)));
        } catch (EntityNotFoundException e) {
            return false;
        }

        return trainer.getUser().getPassword().equals(password);
    }

    @Override
    public TrainerDto changePassword(String userName, String newPassword) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName)));
        trainer.getUser().setPassword(newPassword);
        return trainerMapper.convertToDto(trainerRepository.save(trainer));
    }

    @Override
    public List<TrainerDto> getUnassignedTrainersList(String traineeUserName) throws EntityNotFoundException {
        Trainee existingTrainee = traineeRepository.findByUserName(traineeUserName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, traineeUserName))
        );
        List<Trainer> trainers = trainerRepository.findAll();

        return trainers.stream()
                .filter(trainer -> !trainer.getTrainees().contains(existingTrainee))
                .map(trainerMapper::convertToDto)
                .toList();
    }

    @Override
    public List<TrainerDto> updateTrainersList(String traineeUserName, List<String> trainersUserNames) throws EntityNotFoundException {
        Trainee existingTrainee = traineeRepository.findByUserName(traineeUserName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, traineeUserName))
        );

        List<Trainer> trainers = trainersUserNames.stream()
                .map(trainerRepository::findByUserName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        existingTrainee.setTrainers(trainers);
        traineeRepository.save(existingTrainee);

        return trainers.stream()
                .map(trainerMapper::convertToDto)
                .toList();
    }

    public boolean isFirstOrLastNamesChanged(TrainerDto trainerDto, Trainer oldTrainer) {
        return !oldTrainer.getUser().getFirstName()
                .equals(trainerDto.getUser().getFirstName())
                || !oldTrainer.getUser().getLastName()
                .equals(trainerDto.getUser().getLastName());
    }
}
