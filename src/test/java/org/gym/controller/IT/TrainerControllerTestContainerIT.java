package org.gym.controller.IT;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.controller.TrainerController;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.entity.Trainer;
import org.gym.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@ActiveProfiles("prod")
@WebAppConfiguration
class TrainerControllerTestContainerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQL10Dialect");
        registry.add("hibernate.hbm2ddl.auto", () -> "create");
        registry.add("hibernate.show_sql", () -> true);
        registry.add("hibernate.format_sql", () -> true);
        registry.add("hibernate.jdbc.lob.non_contextual_creation", () -> true);
    }

    @Autowired
    private TrainerController trainerController;

    @Autowired
    private TrainerRepository trainerRepository;

    private String userNameForTrainer;

    @Test
    void createTrainerSuccessfully() {
        CreateResponse createResponse = trainerController.create(DataStorage.trainerDto1);
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
        CreateResponse createResponse = trainerController.create(DataStorage.trainerDto1);
        userNameForTrainer = createResponse.getUserName();
        Trainer createdTrainer = trainerRepository.findByUserName(userNameForTrainer).get();
        TrainerSelectResponse selectedTrainer = trainerController.getTrainerProfile(userNameForTrainer);

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
        CreateResponse createResponse = trainerController.create(DataStorage.trainerDto1);
        userNameForTrainer = createResponse.getUserName();

        TrainerUpdateResponse updatedTrainerResponse =
                trainerController.update(userNameForTrainer, DataStorage.trainerUpdateRequest);
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
