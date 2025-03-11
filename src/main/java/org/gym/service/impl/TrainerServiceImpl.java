package org.gym.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.annotation.GymService;
import org.gym.dto.TrainerDto;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainerMapper;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.TrainerService;
import org.gym.service.UserNameGeneratorService;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE;
import static org.gym.config.Config.ENTITY_NOT_FOUND_MESSAGE_TEMPLATE;

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
        String trainingTypeName = trainerDto.getSpecialization().getTrainingTypeName();
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName)
                .orElseThrow(() -> {
                    LOGGER.debug(ENTITY_NOT_FOUND_MESSAGE_TEMPLATE, trainingTypeName);
                    return new EntityNotFoundException(
                            String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, trainingTypeName));
                });
        Trainer trainer = trainerMapper.convertToEntity(trainerDto);
        trainer.getUser().setUserName(
                userNameGeneratorService.generate(
                        trainerDto.getUser().getFirstName(),
                        trainerDto.getUser().getLastName()
                ));
        trainer.setSpecialization(trainingType);
        trainer.getUser().setPassword(passwordGeneratorService.generate());
        if(trainer.getUser().getIsActive() == null) {
            trainer.getUser().setIsActive(true);
        }
        Trainer savedTrainer = trainerRepository.save(trainer);
        LOGGER.info("Trainer with username {} created successfully", savedTrainer.getUser().getUserName());
        return trainerMapper.convertToCreateResponse(savedTrainer);
    }

    @Override
    public TrainerSelectResponse select(String userName) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> {
                    LOGGER.debug(ENTITY_NOT_FOUND_MESSAGE_TEMPLATE, userName);
                    return new EntityNotFoundException(
                            String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, userName));
                });
        LOGGER.debug("Trainer with userName {} was successfully selected", trainer.getUser().getUserName());
        return trainerMapper.convertToTrainerSelectResponse(trainer);
    }

    @Override
    public TrainerUpdateResponse update(String userName, TrainerUpdateRequest trainerUpdateRequest) throws EntityNotFoundException {
        Trainer oldTrainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> {
                    LOGGER.debug(ENTITY_NOT_FOUND_MESSAGE_TEMPLATE, userName);
                    return new EntityNotFoundException(
                            String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, userName));
                });

        oldTrainer.getUser().setFirstName(trainerUpdateRequest.getUser().getFirstName());
        oldTrainer.getUser().setLastName(trainerUpdateRequest.getUser().getLastName());
        oldTrainer.getUser().setIsActive(trainerUpdateRequest.getUser().getIsActive());
        String trainingTypeName = trainerUpdateRequest.getSpecialization().getTrainingTypeName();
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName)
                .orElseThrow(() -> {
                    LOGGER.debug(ENTITY_NOT_FOUND_MESSAGE_TEMPLATE, trainingTypeName);
                    return new EntityNotFoundException(
                                    String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, trainingTypeName));
                });

        oldTrainer.setSpecialization(trainingType);
        Trainer trainer = trainerRepository.save(oldTrainer);
        LOGGER.info("Trainer with username {} updated successfully", trainer.getUser().getUserName());
        return trainerMapper.convertTrainerToTrainerUpdateResponse(trainer);
    }

    @Override
    public TrainerDto changeSpecialization(String userName, TrainingType trainingType) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> {
                    LOGGER.debug(ENTITY_NOT_FOUND_MESSAGE_TEMPLATE, userName);
                    return new EntityNotFoundException(
                            String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, userName));
                });
        trainer.setSpecialization(trainingType);
        LOGGER.info("Trainer with username {}; specialization was changed successfully", trainer.getUser().getUserName());
        return trainerMapper.convertToDto(trainerRepository.save(trainer));
    }
}
