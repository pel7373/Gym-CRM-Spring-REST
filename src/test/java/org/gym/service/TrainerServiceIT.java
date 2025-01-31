package org.gym.service;

import org.gym.config.Config;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.entity.Trainer;
import org.gym.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@jakarta.transaction.Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainerServiceIT {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerRepository trainerRepository;

    private final TrainerDto trainerDto;
    private final TrainerDto trainerDto2;
    private String userNameForTrainer;

    {
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        UserDto userDto2 = new UserDto("Petro", "Ivanenko", "Petro.Ivanenko", true);

        trainerDto = TrainerDto.builder()
                .user(userDto)
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        trainerDto2 = TrainerDto.builder()
                .user(userDto2)
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();
    }

    @Test
    void createTrainerSuccessfully() {
        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());
        assertAll(
                "Grouped assertions of created trainerDto",
                () -> assertEquals("Maria", createdTrainerDto.getUser().getFirstName(),
                        "firstName should be equal"),
                () -> assertEquals("Petrenko", createdTrainerDto.getUser().getLastName(),
                        "lastName should be equal"),
                () -> assertEquals(trainerDto.getUser().getIsActive(), createdTrainerDto.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(trainerDto.getSpecialization(), createdTrainerDto.getSpecialization(),
                        "specialization should be equal")
        );

        Trainer createdTrainer = trainerRepository.findByUserName(createdTrainerDto.getUser().getUserName()).get();
        assertNotNull(createdTrainer);
        assertNotNull(createdTrainer.getUser());
        assertAll(
                "Grouped assertions of created trainer",
                () -> assertEquals("Maria", createdTrainer.getUser().getFirstName(),
                        "firstName should be equal"),
                () -> assertEquals("Petrenko", createdTrainer.getUser().getLastName(),
                        "lastName should be equal"),
                () -> assertEquals(trainerDto.getUser().getIsActive(), createdTrainer.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(trainerDto.getSpecialization().getTrainingTypeName(),
                        createdTrainer.getSpecialization().getTrainingTypeName(),
                        "specialization should be equal")
        );
    }

    @Test
    void selectTrainerSuccessfully() {
        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto selectedTrainerDto = trainerService.select(userNameForTrainer);

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());
        assertNotNull(selectedTrainerDto);
        assertNotNull(selectedTrainerDto);
        assertAll(
                "Grouped assertions of selected trainerDto",
                () -> assertEquals("Maria", createdTrainerDto.getUser().getFirstName(),
                        "firstName should be equal"),
                () -> assertEquals("Petrenko", createdTrainerDto.getUser().getLastName(),
                        "lastName should be equal"),
                () -> assertEquals(trainerDto.getUser().getIsActive(), createdTrainerDto.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(trainerDto.getSpecialization(), createdTrainerDto.getSpecialization(),
                        "specialization should be equal")
        );

        Trainer selectedTrainer = trainerRepository.findByUserName(selectedTrainerDto.getUser().getUserName()).get();
        assertNotNull(selectedTrainer);
        assertNotNull(selectedTrainer.getUser());
        assertAll(
                "Grouped assertions of created trainer",
                () -> assertEquals("Maria", selectedTrainer.getUser().getFirstName(), "firstName should be equal"),
                () -> assertEquals("Petrenko", selectedTrainer.getUser().getLastName(), "lastName should be equal"),
                () -> assertEquals(trainerDto.getUser().getIsActive(), selectedTrainer.getUser().getIsActive(), "isActive should be equal"),
                () -> assertEquals(trainerDto.getSpecialization().getTrainingTypeName(), selectedTrainer.getSpecialization().getTrainingTypeName(), "specialization should be equal")
        );
    }

    @Test
    void updateTrainerSuccessfully() {
        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto updatedTrainerDto = trainerService.update(userNameForTrainer, trainerDto2);
        userNameForTrainer = updatedTrainerDto.getUser().getUserName();

        assertNotNull(updatedTrainerDto);
        assertNotNull(updatedTrainerDto.getUser());
        assertAll(
                "Grouped assertions of selected trainerDto",
                () -> assertEquals("Petro", updatedTrainerDto.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals("Ivanenko", updatedTrainerDto.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertEquals(trainerDto.getUser().getIsActive(), updatedTrainerDto.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(trainerDto.getSpecialization(), updatedTrainerDto.getSpecialization(),
                        "specialization should be equal")
        );

        Trainer updatedTrainer = trainerRepository.findByUserName(updatedTrainerDto.getUser().getUserName()).get();
        assertNotNull(updatedTrainer);
        assertNotNull(updatedTrainer.getUser());
        assertAll(
                "Grouped assertions of created trainer",
                () -> assertEquals("Petro", updatedTrainer.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals("Ivanenko", updatedTrainer.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertEquals(trainerDto.getUser().getIsActive(), updatedTrainer.getUser().getIsActive(), "isActive should be equal"),
                () -> assertEquals(trainerDto.getSpecialization().getTrainingTypeName(),
                        updatedTrainer.getSpecialization().getTrainingTypeName(),
                        "specialization should be equal")
        );
    }

    @Test
    void changeStatusSuccessfully() {
        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());

        boolean oldStatus = createdTrainerDto.getUser().getIsActive();
        boolean newStatus = !oldStatus;
        TrainerDto changedTrainerDto = trainerService.changeStatus(userNameForTrainer, newStatus);

        assertNotNull(changedTrainerDto);
        assertNotNull(changedTrainerDto.getUser());
        assertEquals(newStatus, changedTrainerDto.getUser().getIsActive());
    }

    @Test
    void changeStatusTheSecondTimeDoesntChange() {
        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());

        boolean oldStatus = createdTrainerDto.getUser().getIsActive();
        boolean newStatus = !oldStatus;
        TrainerDto changedTrainerDto = trainerService.changeStatus(userNameForTrainer, newStatus);

        assertNotNull(changedTrainerDto);
        assertNotNull(changedTrainerDto.getUser());
        assertEquals(newStatus, changedTrainerDto.getUser().getIsActive());

        TrainerDto changedagainTrainerDto = trainerService.changeStatus(userNameForTrainer, newStatus);

        assertNotNull(changedTrainerDto);
        assertNotNull(changedTrainerDto.getUser());
        assertEquals(newStatus, changedagainTrainerDto.getUser().getIsActive());
    }

    @Test
    void changePasswordSuccessfully() {
        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());

        String newPassword = "1111111111";

        TrainerDto changedTrainerDto = trainerService.changePassword(userNameForTrainer, newPassword);
        String changedPassword = trainerRepository.findByUserName(userNameForTrainer).get().getUser().getPassword();

        assertNotNull(changedTrainerDto);
        assertNotNull(changedTrainerDto.getUser());
        assertEquals(newPassword, changedPassword);
    }
}
