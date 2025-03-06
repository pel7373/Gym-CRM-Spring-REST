package org.gym.controller.IT;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.controller.TraineeController;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@ActiveProfiles("prod")
@WebAppConfiguration
public class TraineeControllerTestContainerIT {

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
    private TraineeController traineeController;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    private final DataStorage ds = new DataStorage();
    private String userNameForTrainee;

    @Test
    void createTraineeSuccessfully() {
        CreateResponse createResponse = traineeController.create(ds.traineeDto);
        userNameForTrainee = ds.traineeDto.getUser().getUserName();
        Trainee createdTrainee = traineeRepository.findByUserName(userNameForTrainee).get();

        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertNotNull(createResponse),
                () -> assertNotNull(createdTrainee),
                () -> assertNotNull(createdTrainee.getUser()),
                () -> assertEquals(ds.traineeDto.getUser().getFirstName(), createdTrainee.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals(ds.traineeDto.getUser().getLastName(), createdTrainee.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertTrue(createdTrainee.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals(ds.traineeDto.getAddress(), createdTrainee.getAddress(),
                        "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );
    }

    @Test
    void selectTraineeSuccessfully() {
        CreateResponse createResponse = traineeController.create(ds.traineeDto);
        userNameForTrainee = createResponse.getUserName();
        String passwordForCreatedTrainee = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();
        TraineeSelectResponse select = traineeController.getTraineeProfile(userNameForTrainee);
        String passwordForSelectedTrainee = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();

        assertAll(
                "Grouped assertions of selected traineeDto",
                () -> assertNotNull(createResponse),
                () -> assertNotNull(select),
                () -> assertNotNull(select.getUser()),
                () -> assertEquals(ds.traineeDto.getUser().getFirstName(), select.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals(ds.traineeDto.getUser().getLastName(), select.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertEquals(passwordForCreatedTrainee, passwordForSelectedTrainee,
                        "password should be equal"),
                () -> assertTrue(select.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals(ds.traineeDto.getAddress(), select.getAddress(),
                        "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );

        Trainee selectedTrainee = traineeRepository.findByUserName(userNameForTrainee).get();
        assertNotNull(selectedTrainee);
        assertNotNull(selectedTrainee.getUser());
        assertAll(
                "Grouped assertions of created trainee",
                () -> assertEquals(ds.traineeDto.getUser().getFirstName(), selectedTrainee.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals(ds.traineeDto.getUser().getLastName(), selectedTrainee.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertEquals(passwordForCreatedTrainee, passwordForSelectedTrainee,
                        "password should be equal"),
                () -> assertTrue(selectedTrainee.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals(ds.traineeDto.getAddress(), selectedTrainee.getAddress(),
                        "address should be equal")
        );
    }

    @Test
    void updateTraineeSuccessfully() {
        CreateResponse createResponse = traineeController.create(ds.traineeDto);
        userNameForTrainee = createResponse.getUserName();
        TraineeUpdateResponse traineeUpdateResponse = traineeController.update(userNameForTrainee, ds.traineeUpdateRequest);
        userNameForTrainee = traineeUpdateResponse.getUser().getUserName();

        assertAll(
                "Grouped assertions of selected traineeDto",
                () -> assertNotNull(traineeUpdateResponse),
                () -> assertNotNull(traineeUpdateResponse.getUser()),
                () -> assertEquals(ds.traineeUpdateRequest.getUser().getFirstName(), traineeUpdateResponse.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals(ds.traineeUpdateRequest.getUser().getLastName(), traineeUpdateResponse.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertTrue(traineeUpdateResponse.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals(ds.traineeUpdateRequest.getAddress(), traineeUpdateResponse.getAddress(),
                        "address should be equal")
        );

        Trainee updatedTrainee = traineeRepository.findByUserName(ds.traineeDto.getUser().getUserName()).get();
        assertNotNull(updatedTrainee);
        assertNotNull(updatedTrainee.getUser());
        assertAll(
                "Grouped assertions of created trainee",
                () -> assertEquals(ds.traineeUpdateRequest.getUser().getFirstName(), updatedTrainee.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals(ds.traineeUpdateRequest.getUser().getLastName(), updatedTrainee.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertTrue(updatedTrainee.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals(ds.traineeUpdateRequest.getAddress(), updatedTrainee.getAddress(),
                        "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );
    }

    @Test
    void deleteTraineeSuccessfully() {
        CreateResponse createResponse = traineeController.create(ds.traineeDto);
        userNameForTrainee = createResponse.getUserName();

        assertNotNull(createResponse);
        traineeController.delete(userNameForTrainee);
        assertDoesNotThrow(() -> traineeRepository.findByUserName(userNameForTrainee));
    }


    @Test
    void getUnassignedTrainersListSuccessfully() {
        String trainingTypeNameTrainer = "Zumba";
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeNameTrainer).get();
        ds.trainer1.setSpecialization(trainingType);
        ds.trainer2.setSpecialization(trainingType);
        Trainee createdTrainee1 = traineeRepository.save(ds.trainee1);
        Trainer createdTrainer1 = trainerRepository.save(ds.trainer1);
        Trainer createdTrainer2 = trainerRepository.save(ds.trainer2);
        createdTrainee1.setTrainers(List.of(createdTrainer1));

        List<TrainerForListResponse> unassignedTrainersList = traineeController.getUnassignedTrainers(ds.traineeUserName);

        assertAll(
                "Grouped assertions of getUnassigned trainers' list",
                () -> assertNotNull(unassignedTrainersList),
                () -> assertEquals(1, unassignedTrainersList.size()),
                () -> assertEquals(createdTrainer2.getUser().getUserName(), unassignedTrainersList.get(0).getUser().getUserName())
        );
    }

    @Test
    void getUnassignedTrainersListEmpty() {
        String trainingTypeNameTrainer = "Zumba";
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeNameTrainer).get();
        ds.trainer1.setSpecialization(trainingType);
        Trainee createdTrainee1 = traineeRepository.save(ds.trainee1);
        Trainer createdTrainer1 = trainerRepository.save(ds.trainer1);
        createdTrainee1.setTrainers(List.of(createdTrainer1));

        List<TrainerForListResponse> unassignedTrainersList = traineeController.getUnassignedTrainers(ds.traineeUserName);

        assertAll(
                "Grouped assertions of getUnassigned trainersDto's list",
                () -> assertNotNull(unassignedTrainersList),
                () -> assertEquals(0, unassignedTrainersList.size())
        );
    }

    @Test
    void updateTrainersListSuccessfully() {
        String trainingTypeNameTrainer = "Zumba";
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeNameTrainer).get();
        ds.trainer1.setSpecialization(trainingType);
        ds.trainer2.setSpecialization(trainingType);

        Trainee createdTrainee1 = traineeRepository.save(ds.trainee1);
        Trainer createdTrainer1 = trainerRepository.save(ds.trainer1);
        Trainer createdTrainer2 = trainerRepository.save(ds.trainer2);

        assertAll(
                "Grouped assertions of updateTrainersList successfully",
                () -> assertNull(createdTrainee1.getTrainers())
        );

        List<String> trainersList = List.of(
                createdTrainer1.getUser().getUserName(),
                createdTrainer2.getUser().getUserName());

        List<TrainerForListResponse> trainerForListResponses
                = traineeController.updateTrainersList(ds.traineeUserName, trainersList);

        Trainee checkTrainee = traineeRepository.findByUserName(ds.traineeUserName).get();

        assertAll(
                "Grouped assertions of updateTrainersList successfully",
                () -> assertNotNull(checkTrainee.getTrainers()),
                () -> assertEquals(2, checkTrainee.getTrainers().size()),
                () -> assertNotNull(trainerForListResponses),
                () -> assertEquals(2, trainerForListResponses.size())
        );
    }
}
