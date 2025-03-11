package org.gym.service.IT;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.Trainer;
import org.gym.repository.TrainerRepository;
import org.gym.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebAppConfiguration
class TrainerServiceIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerRepository trainerRepository;

    private String userNameForTrainer;

    @Test
    void createTrainerSuccessfully() {
        CreateResponse createResponse = trainerService.create(DataStorage.trainerDto1);
        userNameForTrainer = DataStorage.trainerDto1.getUser().getUserName();
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
                () -> assertEquals(DataStorage.trainerDto1.getUser().getFirstName(), createdTrainer.getUser().getFirstName(),
                        "firstName should be equal"),
                () -> assertEquals(DataStorage.trainerDto1.getUser().getLastName(), createdTrainer.getUser().getLastName(),
                        "lastName should be equal"),
                () -> assertEquals(DataStorage.trainerDto1.getUser().getIsActive(), createdTrainer.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(DataStorage.trainerDto1.getSpecialization().getTrainingTypeName(), createdTrainer.getSpecialization().getTrainingTypeName(),
                        "specialization should be equal")
        );
    }

    @Test
    void selectTrainerSuccessfully() {
        CreateResponse createResponse = trainerService.create(DataStorage.trainerDto1);
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
                () -> assertEquals(createdTrainer.getSpecialization().getTrainingTypeName(),
                        selectedTrainer.getSpecialization(),
                        "specialization should be equal")
        );
    }

    @Test
    void updateTrainerSuccessfully() {
        CreateResponse createResponse = trainerService.create(DataStorage.trainerDto1);
        userNameForTrainer = createResponse.getUserName();

        TrainerUpdateResponse updatedTrainerResponse =
                trainerService.update(userNameForTrainer, DataStorage.trainerUpdateRequest);
        String userNameForUpdatedTrainer = updatedTrainerResponse.getUser().getUserName();
        Trainer updatedTrainer = trainerRepository.findByUserName(userNameForTrainer).get();

        assertAll(
                "Grouped assertions of updated trainer",
                () -> assertNotNull(updatedTrainer),
                () -> assertNotNull(updatedTrainer.getUser()),
                () -> assertNotNull(updatedTrainerResponse),
                () -> assertNotNull(updatedTrainerResponse.getUser()),
                () -> assertEquals(userNameForTrainer, userNameForUpdatedTrainer,
                        "userName for created and then updated trainer must be equal"),
                () -> assertEquals(DataStorage.trainerUpdateRequest.getUser().getFirstName(),
                        updatedTrainer.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals(DataStorage.trainerUpdateRequest.getUser().getLastName(),
                        updatedTrainer.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertEquals(DataStorage.trainerUpdateRequest.getUser().getIsActive(),
                        updatedTrainer.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(DataStorage.trainerUpdateRequest.getSpecialization().getTrainingTypeName(),
                        updatedTrainer.getSpecialization().getTrainingTypeName(),
                        "specialization should be equal")
        );
    }
}
