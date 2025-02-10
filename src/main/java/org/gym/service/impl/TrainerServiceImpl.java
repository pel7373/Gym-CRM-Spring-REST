package org.gym.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TrainerDto;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainerMapper;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.TrainerService;
import org.gym.service.UserNameGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
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
        LOGGER.info("begin: {}", trainerDto);
        trainerDto.getUser().setUserName(
                userNameGeneratorService.generate(
                        trainerDto.getUser().getFirstName(),
                        trainerDto.getUser().getLastName()
                ));

        String trainingTypeName = trainerDto.getSpecialization().getTrainingTypeName();
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, trainingTypeName)));
        LOGGER.info("2: {}", trainingType);
        Trainer trainer = trainerMapper.convertToEntity(trainerDto);
        LOGGER.info("3: {}", trainer);
        trainer.setSpecialization(trainingType);
        trainer.getUser().setPassword(passwordGeneratorService.generate());
        LOGGER.info("4: {}", trainer);
        Trainer savedTrainer = trainerRepository.save(trainer);
        LOGGER.info("5: {}", trainer);
        return trainerMapper.convertToDto(savedTrainer);
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
        try {
            Trainer trainer = trainerRepository.findByUserName(userName)
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format(ENTITY_NOT_FOUND_EXCEPTION, userName)));
            return trainer.getUser().getPassword().equals(password);
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    @Override
    public TrainerDto changePassword(String userName, String newPassword) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName)));
        trainer.getUser().setPassword(newPassword);
        return trainerMapper.convertToDto(trainerRepository.save(trainer));
    }

    public boolean isFirstOrLastNamesChanged(TrainerDto trainerDto, Trainer oldTrainer) {
        return !oldTrainer.getUser().getFirstName()
                .equals(trainerDto.getUser().getFirstName())
                || !oldTrainer.getUser().getLastName()
                .equals(trainerDto.getUser().getLastName());
    }
}
