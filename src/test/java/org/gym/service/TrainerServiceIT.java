package org.gym.service;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@jakarta.transaction.Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainerServiceIT {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerRepository trainerRepository;

    private final DataStorage ds = new DataStorage();
//    private final TrainerDto trainerDto;
//    private final TrainerDto trainerDto2;
    private String userNameForTrainer;

    {
//        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
//        UserDto userDto2 = new UserDto("Petro", "Ivanenko", "Petro.Ivanenko", true);
//
//        trainerDto = TrainerDto.builder()
//                .user(userDto)
//                .specialization(TrainingTypeDto.builder()
//                        .trainingTypeName("Zumba")
//                        .build())
//                .build();
//
//        trainerDto2 = TrainerDto.builder()
//                .user(userDto2)
//                .specialization(TrainingTypeDto.builder()
//                        .trainingTypeName("Zumba")
//                        .build())
//                .build();
    }

    @Test
    void createTrainerSuccessfully() {
        CreateResponse createResponse = trainerService.create(ds.trainerDto1);
        userNameForTrainer = ds.trainerDto1.getUser().getUserName();
        Trainer createdTrainer = trainerRepository.findByUserName(userNameForTrainer).get();

        assertAll(
                "Grouped assertions of created trainerDto",
                () -> assertNotNull(createResponse),
                () -> assertNotNull(createdTrainer),
                () -> assertNotNull(createdTrainer.getUser()),
                () -> assertEquals(createResponse.getUserName(), createdTrainer.getUser().getUserName(),
                        "userName should be equal"),
                () -> assertEquals(createResponse.getPassword(), createdTrainer.getUser().getPassword(),
                        "password should be equal"),
                () -> assertEquals(ds.trainerDto1.getUser().getFirstName(), createdTrainer.getUser().getFirstName(),
                        "firstName should be equal"),
                () -> assertEquals(ds.trainerDto1.getUser().getLastName(), createdTrainer.getUser().getLastName(),
                        "lastName should be equal"),
                () -> assertEquals(ds.trainerDto1.getUser().getIsActive(), createdTrainer.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(ds.trainerDto1.getSpecialization(), createdTrainer.getSpecialization(),
                        "specialization should be equal")
        );
    }

    @Test
    void selectTrainerSuccessfully() {
        CreateResponse createResponse = trainerService.create(ds.trainerDto1);
        userNameForTrainer = createResponse.getUserName();
        Trainer createdTrainer = trainerRepository.findByUserName(userNameForTrainer).get();
        TrainerSelectResponse selectedTrainer = trainerService.select(userNameForTrainer);

        assertAll(
                "Grouped assertions of selected trainer",
                () -> assertNotNull(createdTrainer),
                () -> assertNotNull(createdTrainer.getUser()),
                () -> assertNotNull(selectedTrainer),
                () -> assertNotNull(selectedTrainer.getUser()),
                () -> assertEquals(createdTrainer.getUser().getFirstName(), selectedTrainer.getUser().getFirstName(),
                        "firstName should be equal"),
                () -> assertEquals(createdTrainer.getUser().getLastName(), selectedTrainer.getUser().getLastName(),
                        "lastName should be equal"),
                () -> assertEquals(createdTrainer.getUser().getIsActive(), selectedTrainer.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(createdTrainer.getSpecialization(), selectedTrainer.getSpecialization(),
                        "specialization should be equal")
        );
    }

    @Test
    void updateTrainerSuccessfully() {
        CreateResponse createResponse = trainerService.create(ds.trainerDto1);
        userNameForTrainer = createResponse.getUserName();

        TrainerUpdateResponse updatedTrainerResponse =
                trainerService.update(userNameForTrainer, ds.trainerUpdateRequest);
        String userNameForUpdatedTrainer = updatedTrainerResponse.getUser().getUserName();
        Trainer updatedTrainer = trainerRepository.findByUserName(userNameForTrainer).get();

        assertAll(
                "Grouped assertions of selected trainerDto",
                () -> assertNotNull(updatedTrainer),
                () -> assertNotNull(updatedTrainer.getUser()),
                () -> assertNotNull(updatedTrainerResponse),
                () -> assertNotNull(updatedTrainerResponse.getUser()),
                () -> assertEquals(userNameForTrainer, userNameForUpdatedTrainer,
                        "userName for created and then updated trainer must be equal"),
                () -> assertEquals(ds.trainerUpdateRequest.getUser().getFirstName(),
                        updatedTrainer.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals(ds.trainerUpdateRequest.getUser().getLastName(),
                        updatedTrainer.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertEquals(ds.trainerUpdateRequest.getUser().getIsActive(),
                        updatedTrainer.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(ds.trainerUpdateRequest.getSpecialization(),
                        updatedTrainer.getSpecialization(),
                        "specialization should be equal")
        );
    }

//    @Test
//    void changeStatusSuccessfully() {
//        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
//        userNameForTrainer = createdTrainerDto.getUser().getUserName();
//
//        assertNotNull(createdTrainerDto);
//        assertNotNull(createdTrainerDto.getUser());
//
//        boolean oldStatus = createdTrainerDto.getUser().getIsActive();
//        boolean newStatus = !oldStatus;
//        TrainerDto changedTrainerDto = trainerService.changeStatus(userNameForTrainer, newStatus);
//
//        assertNotNull(changedTrainerDto);
//        assertNotNull(changedTrainerDto.getUser());
//        assertEquals(newStatus, changedTrainerDto.getUser().getIsActive());
//    }
//
//    @Test
//    void changeStatusTheSecondTimeDoesntChange() {
//        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
//        userNameForTrainer = createdTrainerDto.getUser().getUserName();
//
//        assertNotNull(createdTrainerDto);
//        assertNotNull(createdTrainerDto.getUser());
//
//        boolean oldStatus = createdTrainerDto.getUser().getIsActive();
//        boolean newStatus = !oldStatus;
//        TrainerDto changedTrainerDto = trainerService.changeStatus(userNameForTrainer, newStatus);
//
//        assertNotNull(changedTrainerDto);
//        assertNotNull(changedTrainerDto.getUser());
//        assertEquals(newStatus, changedTrainerDto.getUser().getIsActive());
//
//        TrainerDto changedagainTrainerDto = trainerService.changeStatus(userNameForTrainer, newStatus);
//
//        assertNotNull(changedTrainerDto);
//        assertNotNull(changedTrainerDto.getUser());
//        assertEquals(newStatus, changedagainTrainerDto.getUser().getIsActive());
//    }
//
//    @Test
//    void changePasswordSuccessfully() {
//        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
//        userNameForTrainer = createdTrainerDto.getUser().getUserName();
//
//        assertNotNull(createdTrainerDto);
//        assertNotNull(createdTrainerDto.getUser());
//
//        String newPassword = "1111111111";
//
//        TrainerDto changedTrainerDto = trainerService.changePassword(userNameForTrainer, newPassword);
//        String changedPassword = trainerRepository.findByUserName(userNameForTrainer).get().getUser().getPassword();
//
//        assertNotNull(changedTrainerDto);
//        assertNotNull(changedTrainerDto.getUser());
//        assertEquals(newPassword, changedPassword);
//    }
}
