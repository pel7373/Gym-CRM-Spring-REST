package org.gym.service.impl;

import org.gym.annotation.GymService;
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
        Trainee trainee = traineeMapper.convertToEntity(traineeDto);
        traineeDto.getUser().setUserName(
                userNameGeneratorService.generate(
                        traineeDto.getUser().getFirstName(),
                        traineeDto.getUser().getLastName()
                ));

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
        LOGGER.debug("trainee was selected successfully for userName {}", trainee.getUser().getUserName());
        return traineeMapper.convertTraineeToTraineeSelectResponse(trainee);
    }

    @Override
    public TraineeUpdateResponse update(String userName, TraineeUpdateRequest traineeUpdateRequest) throws EntityNotFoundException {
        Trainee oldTrainee = traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
        );
        oldTrainee.getUser().setFirstName(traineeUpdateRequest.getUser().getFirstName());
        oldTrainee.getUser().setLastName( traineeUpdateRequest.getUser().getLastName());
        oldTrainee.getUser().setIsActive( traineeUpdateRequest.getUser().getIsActive());
        if(traineeUpdateRequest.getDateOfBirth() != null) {
            oldTrainee.setDateOfBirth(traineeUpdateRequest.getDateOfBirth());
        }
        if(traineeUpdateRequest.getAddress() != null) {
            oldTrainee.setAddress(traineeUpdateRequest.getAddress());
        }

        Trainee trainee = traineeRepository.save(oldTrainee);
        return traineeMapper.convertTraineeToTraineeUpdateResponse(trainee);
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

        List<Trainer> trainerUnassignedList =
                trainerRepository.findAll().stream()
                .filter(trainer -> !trainer.getTrainees().contains(trainee))
                .toList();

        LOGGER.debug("list created successfully for userName {}", userName);
        return trainerMapper.convertTrainerListToTrainerForListResponseList(trainerUnassignedList);
    }

    @Override
    public List<TrainerForListResponse> updateTrainersList(String userName, List<String> listTrainersUserNames) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
                );

        List<Trainer> trainerList = listTrainersUserNames.stream()
                .map(trainerRepository::findByUserName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        trainee.setTrainers(trainerList);

        List<TrainerForListResponse> trainerForListResponses = trainerMapper.convertTrainerListToTrainerForListResponseList(trainerList);
        LOGGER.debug("updated successfully for userName {}", userName);
        return trainerForListResponses;
    }
}
