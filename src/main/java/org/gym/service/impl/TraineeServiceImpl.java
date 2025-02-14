package org.gym.service.impl;

import org.gym.dto.TrainerDto;
import org.gym.entity.Trainer;
import org.gym.exception.EntityNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.entity.Trainee;
import org.gym.dto.TraineeDto;
import org.gym.mapper.TraineeMapper;
import org.gym.mapper.TrainerMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.TraineeService;
import org.gym.service.UserNameGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final UserNameGeneratorService userNameGeneratorService;
    private final PasswordGeneratorService passwordGeneratorService;

    @Override
    public TraineeDto create(TraineeDto traineeDto) {
        traineeDto.getUser().setUserName(
                userNameGeneratorService.generate(
                        traineeDto.getUser().getFirstName(),
                        traineeDto.getUser().getLastName()
                ));

        Trainee trainee = traineeMapper.convertToEntity(traineeDto);
        trainee.getUser().setPassword(passwordGeneratorService.generate());
        Trainee savedTrainee = traineeRepository.save(trainee);
        return traineeMapper.convertToDto(savedTrainee);
    }

    @Override
    public TraineeDto select(String userName) throws EntityNotFoundException {
        return traineeMapper.convertToDto(traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
        ));
    }

    @Override
    public TraineeDto update(String userName, TraineeDto traineeDto) throws EntityNotFoundException {
        Trainee oldTrainee = traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
        );
        oldTrainee.getUser().setFirstName(traineeDto.getUser().getFirstName());
        oldTrainee.getUser().setLastName(traineeDto.getUser().getLastName());
        oldTrainee.getUser().setIsActive(traineeDto.getUser().getIsActive());
        if(traineeDto.getDateOfBirth() != null) {
            oldTrainee.setDateOfBirth(traineeDto.getDateOfBirth());
        }
        if(traineeDto.getAddress() != null) {
            oldTrainee.setAddress(traineeDto.getAddress());
        }

        Trainee trainee = traineeRepository.save(oldTrainee);
        return traineeMapper.convertToDto(trainee);
    }

    @Override
    public void delete(String userName) {
        traineeRepository.delete(userName);
    }

    @Override
    public TraineeDto changeStatus(String userName, Boolean isActive) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
        );
        trainee.getUser().setIsActive(isActive);
        return traineeMapper.convertToDto(traineeRepository.save(trainee));
    }

    @Override
    public boolean authenticate(String userName, String password) {
        Trainee trainee;
        try {
            trainee = traineeRepository.findByUserName(userName)
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
            );
        } catch (EntityNotFoundException e) {
            return false;
        }

        return trainee.getUser().getPassword().equals(password);
    }

    @Override
    public TraineeDto changePassword(String userName, String newPassword) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
        );
        trainee.getUser().setPassword(newPassword);
        return traineeMapper.convertToDto(traineeRepository.save(trainee));
    }

    @Override
    public List<TrainerDto> getUnassignedTrainersList(String userName) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
                );
        List<Trainer> trainers = trainerRepository.findAll();

        return trainers.stream()
                .filter(trainer -> !trainer.getTrainees().contains(trainee))
                .map(trainerMapper::convertToDto)
                .toList();
    }

    @Override
    public List<TrainerDto> updateTrainersList(String userName, List<String> listTrainersUserNames) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
                );

        List<Trainer> trainers = listTrainersUserNames.stream()
                .map(trainerRepository::findByUserName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        trainee.setTrainers(trainers);

        return trainers.stream()
                .map(trainerMapper::convertToDto)
                .toList();
    }

    public boolean isFirstOrLastNamesChanged(TraineeDto traineeDto, Trainee oldTrainee) {
        return !oldTrainee.getUser().getFirstName()
                .equals(traineeDto.getUser().getFirstName())
                || !oldTrainee.getUser().getLastName()
                .equals(traineeDto.getUser().getLastName());
    }
}
