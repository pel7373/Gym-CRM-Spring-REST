package org.gym.service.impl;

import org.gym.exception.EntityNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.entity.Trainee;
import org.gym.dto.TraineeDto;
import org.gym.mapper.TraineeMapper;
import org.gym.repository.TraineeRepository;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.TraineeService;
import org.gym.service.UserNameGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
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
        if(isFirstOrLastNamesChanged(traineeDto, oldTrainee)) {
            oldTrainee.getUser().setUserName(
                    userNameGeneratorService
                            .generate(traineeDto.getUser().getFirstName(),
                                    traineeDto.getUser().getLastName()
                            )
            );
            oldTrainee.getUser().setFirstName(traineeDto.getUser().getFirstName());
            oldTrainee.getUser().setLastName(traineeDto.getUser().getLastName());
        }
        oldTrainee.getUser().setIsActive(traineeDto.getUser().getIsActive());
        oldTrainee.setDateOfBirth(traineeDto.getDateOfBirth());
        oldTrainee.setAddress(traineeDto.getAddress());
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

    public boolean isFirstOrLastNamesChanged(TraineeDto traineeDto, Trainee oldTrainee) {
        return !oldTrainee.getUser().getFirstName()
                .equals(traineeDto.getUser().getFirstName())
                || !oldTrainee.getUser().getLastName()
                .equals(traineeDto.getUser().getLastName());
    }
}
