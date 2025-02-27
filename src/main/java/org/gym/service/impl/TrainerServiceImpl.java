package org.gym.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.annotation.GymService;
import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainerMapper;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.TrainerService;
import org.gym.service.UserNameGeneratorService;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
@GymService
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserNameGeneratorService userNameGeneratorService;
    private final PasswordGeneratorService passwordGeneratorService;
    private final TrainerMapper trainerMapper;

    @Override
    public CreateResponse create(TrainerDto trainerDto) {
        trainerDto.getUser().setUserName(
                userNameGeneratorService.generate(
                        trainerDto.getUser().getFirstName(),
                        trainerDto.getUser().getLastName()
                ));

        String trainingTypeName = trainerDto.getSpecialization().getTrainingTypeName();
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, trainingTypeName)));
        Trainer trainer = trainerMapper.convertToEntity(trainerDto);
        trainer.setSpecialization(trainingType);
        trainer.getUser().setPassword(passwordGeneratorService.generate());
        if(trainer.getUser().getIsActive() == null) {
            trainer.getUser().setIsActive(true);
        }
        Trainer savedTrainer = trainerRepository.save(trainer);
        return trainerMapper.convertToCreateResponse(savedTrainer);
    }

    @Override
    public TrainerSelectResponse select(String userName) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName)));
        LOGGER.debug("selected trainer with userName {}", trainer.getUser().getUserName());
        TrainerDto trainerDto = trainerMapper.convertToDto(trainer);
        return trainerMapper.convertToTrainerSelectResponse(trainerDto);
    }

    @Override
    public TrainerUpdateResponse update(String userName, TrainerUpdateRequest trainerUpdateRequest) throws EntityNotFoundException {
        TrainerDto trainerDto = trainerMapper.convertTrainerUpdateRequestToTrainerDto(trainerUpdateRequest);

        Trainer oldTrainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
        );

        oldTrainer.getUser().setFirstName(trainerDto.getUser().getFirstName());
        oldTrainer.getUser().setLastName(trainerDto.getUser().getLastName());
        oldTrainer.getUser().setIsActive(trainerDto.getUser().getIsActive());
        String trainingTypeName = trainerDto.getSpecialization().getTrainingTypeName();
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName)
                .orElse(null);

        oldTrainer.setSpecialization(trainingType);
        Trainer trainer = trainerRepository.save(oldTrainer);
        TrainerDto updatedTrainerDto = trainerMapper.convertToDto(trainer);
        return trainerMapper.convertDtoToUpdateResponse(updatedTrainerDto);
    }

    @Override
    public TrainerDto changeSpecialization(String userName, TrainingType trainingType) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName)));
        trainer.setSpecialization(trainingType);
        return trainerMapper.convertToDto(trainerRepository.save(trainer));
    }
}
