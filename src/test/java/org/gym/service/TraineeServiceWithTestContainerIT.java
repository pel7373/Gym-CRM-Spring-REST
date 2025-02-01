package org.gym.service;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;
import org.gym.dto.UserDto;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@jakarta.transaction.Transactional
class TraineeServiceWithTestContainerIT {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    private final DataStorage ds = new DataStorage();
    private final TraineeDto traineeDto;
    private final TraineeDto traineeDto2;
    private String userNameForTrainee;

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

    {
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        UserDto userDto2 = new UserDto("Petro", "Ivanenko", "Petro.Ivanenko", true);

        traineeDto = TraineeDto.builder()
                .user(userDto)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        traineeDto2 = TraineeDto.builder()
                .user(userDto2)
                .dateOfBirth(LocalDate.of(1985, 1, 23))
                .address("Kyiv, Soborna str. 35, ap. 26")
                .build();
    }

    @AfterEach
    void destroy() {
        traineeRepository.delete(userNameForTrainee);
    }

    @Test
    void createTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals("Maria", createdTraineeDto.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals("Petrenko", createdTraineeDto.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertTrue(createdTraineeDto.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Vinnitsya, Soborna str. 35, ap. 26", createdTraineeDto.getAddress(),
                        "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );

        Trainee createdTrainee = traineeRepository.findByUserName(createdTraineeDto.getUser().getUserName()).get();
        assertNotNull(createdTrainee);
        assertNotNull(createdTrainee.getUser());
        assertAll(
                "Grouped assertions of created trainee",
                () -> assertEquals("Maria", createdTrainee.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals("Petrenko", createdTrainee.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertTrue(createdTrainee.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Vinnitsya, Soborna str. 35, ap. 26", createdTrainee.getAddress(),
                        "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );
    }

    @Test
    void selectTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        String passwordForCreatedTrainee = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();
        TraineeDto selectedTraineeDto = traineeService.select(userNameForTrainee);
        String passwordForSelectedTrainee = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());
        assertNotNull(selectedTraineeDto);
        assertNotNull(selectedTraineeDto);
        assertAll(
                "Grouped assertions of selected traineeDto",
                () -> assertEquals("Maria", selectedTraineeDto.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals("Petrenko", selectedTraineeDto.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertEquals(passwordForCreatedTrainee, passwordForSelectedTrainee,
                        "password should be equal"),
                () -> assertTrue(selectedTraineeDto.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Vinnitsya, Soborna str. 35, ap. 26",
                        selectedTraineeDto.getAddress(),
                        "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );

        Trainee selectedTrainee = traineeRepository.findByUserName(selectedTraineeDto.getUser().getUserName()).get();
        assertNotNull(selectedTrainee);
        assertNotNull(selectedTrainee.getUser());
        assertAll(
                "Grouped assertions of created trainee",
                () -> assertEquals("Maria", selectedTrainee.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals("Petrenko", selectedTrainee.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertEquals(passwordForCreatedTrainee, passwordForSelectedTrainee,
                        "password should be equal"),
                () -> assertTrue(selectedTrainee.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Vinnitsya, Soborna str. 35, ap. 26", selectedTrainee.getAddress(),
                        "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );
    }

    @Test
    void updateTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto updatedTraineeDto = traineeService.update(userNameForTrainee, traineeDto2);
        userNameForTrainee = updatedTraineeDto.getUser().getUserName();

        assertNotNull(updatedTraineeDto);
        assertNotNull(updatedTraineeDto.getUser());
        assertAll(
                "Grouped assertions of selected traineeDto",
                () -> assertEquals("Petro", updatedTraineeDto.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals("Ivanenko", updatedTraineeDto.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertTrue(updatedTraineeDto.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Kyiv, Soborna str. 35, ap. 26", updatedTraineeDto.getAddress(),
                        "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );

        Trainee updatedTrainee = traineeRepository.findByUserName(updatedTraineeDto.getUser().getUserName()).get();
        assertNotNull(updatedTrainee);
        assertNotNull(updatedTrainee.getUser());
        assertAll(
                "Grouped assertions of created trainee",
                () -> assertEquals("Petro", updatedTrainee.getUser().getFirstName(),
                        "firstName should be Maria"),
                () -> assertEquals("Ivanenko", updatedTrainee.getUser().getLastName(),
                        "lastName should be Petrenko"),
                () -> assertTrue(updatedTrainee.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Kyiv, Soborna str. 35, ap. 26", updatedTrainee.getAddress(),
                        "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );
    }

    @Test
    void deleteTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());
        traineeService.delete(userNameForTrainee);
        assertDoesNotThrow(() -> traineeRepository.findByUserName(userNameForTrainee));
        assertDoesNotThrow(() -> traineeService.delete(userNameForTrainee));
    }

    @Test
    void changeStatusSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());

        boolean oldStatus = createdTraineeDto.getUser().getIsActive();
        boolean newStatus = !oldStatus;
        TraineeDto changedTraineeDto = traineeService.changeStatus(userNameForTrainee, newStatus);

        assertNotNull(changedTraineeDto);
        assertNotNull(changedTraineeDto.getUser());
        assertEquals(newStatus, changedTraineeDto.getUser().getIsActive());
    }

    @Test
    void changeStatusTheSecondTimeDoesntChange() {
        TraineeDto createdTraineeDto = traineeService.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());

        boolean oldStatus = createdTraineeDto.getUser().getIsActive();
        boolean newStatus = !oldStatus;
        TraineeDto changedTraineeDto = traineeService.changeStatus(userNameForTrainee, newStatus);

        assertNotNull(changedTraineeDto);
        assertNotNull(changedTraineeDto.getUser());
        assertEquals(newStatus, changedTraineeDto.getUser().getIsActive());

        TraineeDto changedagainTraineeDto = traineeService.changeStatus(userNameForTrainee, newStatus);

        assertNotNull(changedTraineeDto);
        assertNotNull(changedTraineeDto.getUser());
        assertEquals(newStatus, changedagainTraineeDto.getUser().getIsActive());
    }

    @Test
    void changePasswordSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());

        String newPassword = "1111111111";

        TraineeDto changedTraineeDto = traineeService.changePassword(userNameForTrainee, newPassword);
        String changedPassword = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();

        assertNotNull(changedTraineeDto);
        assertNotNull(changedTraineeDto.getUser());
        assertEquals(newPassword, changedPassword);
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

        List<TrainerDto> unassignedTrainers = traineeService.getUnassignedTrainersList(ds.traineeUserName);

        assertAll(
                "Grouped assertions of getUnassigned trainersDto's list",
                () -> assertNotNull(unassignedTrainers),
                () -> assertEquals(1, unassignedTrainers.size()),
                () -> assertEquals(createdTrainer2.getUser().getUserName(), unassignedTrainers.get(0).getUser().getUserName())
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

        List<TrainerDto> unassignedTrainers = traineeService.getUnassignedTrainersList(ds.traineeUserName);

        assertAll(
                "Grouped assertions of getUnassigned trainersDto's list",
                () -> assertNotNull(unassignedTrainers),
                () -> assertEquals(0, unassignedTrainers.size())
        );
    }
}
