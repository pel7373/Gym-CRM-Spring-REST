package org.gym.service.impl;

import org.gym.annotation.GymService;
import org.gym.dto.TrainerDto;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
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

import java.util.List;
import java.util.Optional;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION;

@Slf4j
@AllArgsConstructor
@GymService
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final UserNameGeneratorService userNameGeneratorService;
    private final PasswordGeneratorService passwordGeneratorService;

    @Override
    public CreateResponse create(TraineeDto traineeDto) {
        traineeDto.getUser().setUserName(
                userNameGeneratorService.generate(
                        traineeDto.getUser().getFirstName(),
                        traineeDto.getUser().getLastName()
                ));

        Trainee trainee = traineeMapper.convertToEntity(traineeDto);
        trainee.getUser().setPassword(passwordGeneratorService.generate());
        if(trainee.getUser().getIsActive() == null) {
            trainee.getUser().setIsActive(true);
        }
        Trainee savedTrainee = traineeRepository.save(trainee);
        return traineeMapper.convertToCreateResponse(savedTrainee);
    }

    @Override
    public TraineeSelectResponse select(String userName) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
                );
        LOGGER.debug("selected trainee with userName {}", trainee.getUser().getUserName());
        TraineeDto traineeDto = traineeMapper.convertToDto(trainee);
        return traineeMapper.convertToTraineeSelectResponse(traineeDto);
    }

    @Override
    public TraineeUpdateResponse update(String userName, TraineeUpdateRequest traineeUpdateRequest) throws EntityNotFoundException {

        TraineeDto traineeDto = traineeMapper.convertTraineeUpdateRequestToTraineeDto(traineeUpdateRequest);

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
        TraineeDto updatedTraineeDto = traineeMapper.convertToDto(trainee);
        return traineeMapper.convertDtoToUpdateResponse(updatedTraineeDto);
    }

    @Override
    public void delete(String userName) throws EntityNotFoundException {
        traineeRepository.delete(userName);
    }


    @Override
    public List<TrainerForListResponse> getUnassignedTrainersList(String userName) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
                );
        List<Trainer> trainers = trainerRepository.findAll();

        List<TrainerDto> trainerDtoList = trainers.stream()
                .filter(trainer -> !trainer.getTrainees().contains(trainee))
                .map(trainerMapper::convertToDto)
                .toList();

//        return trainerMapper.convertTrainerDtoListToTrainerResponseList(trainerDtoList);
        return trainerMapper.convertTrainerDtoListToTrainerResponseList(trainerDtoList);

    }

    @Override
    public List<TrainerForListResponse> updateTrainersList(String userName, List<String> listTrainersUserNames) throws EntityNotFoundException {
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

        List<TrainerDto> trainerDtoList = trainers.stream()
                .map(trainerMapper::convertToDto)
                .toList();

        return trainerMapper.convertTrainerDtoListToTrainerResponseList(trainerDtoList);
    }
}
