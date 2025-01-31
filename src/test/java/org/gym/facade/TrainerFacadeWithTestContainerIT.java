package org.gym.facade;

import org.gym.config.Config;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.entity.Trainer;
import org.gym.facade.impl.TrainerFacadeImpl;
import org.gym.repository.TrainerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@jakarta.transaction.Transactional
class TrainerFacadeWithTestContainerIT {

    @Autowired
    private TrainerFacadeImpl trainerFacade;

    @Autowired
    private TrainerRepository trainerRepository;

    private final TrainerDto trainerDto;
    private final TrainerDto trainerDto2;
    private final TrainerDto trainerDtoNotValid;
    private String userNameForTrainer;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("datasource.url", postgres::getJdbcUrl);
        registry.add("datasource.username", postgres::getUsername);
        registry.add("datasource.password", postgres::getPassword);
        registry.add("hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQL10Dialect");
        registry.add("hibernate.hbm2ddl.auto", () -> "create");
        registry.add("hibernate.show_sql", () -> true);
        registry.add("hibernate.format_sql", () -> true);
        registry.add("hibernate.jdbc.lob.non_contextual_creation", () -> true);
    }

    @Test
    void isPostgresRunningTest() {
        Assertions.assertTrue(postgres.isRunning());
    }

    {
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        UserDto userDto2 = new UserDto("Petro", "Ivanenko", "Petro.Ivanenko", true);
        UserDto userDtoNotValid = new UserDto("Pa", "Pa", "Maria.Petrenko2", false);

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

        trainerDtoNotValid
                = TrainerDto.builder()
                .user(userDtoNotValid)
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();
    }

    @Test
    void createTrainerSuccessfully() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());
        assertAll(
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
    void createTrainerNullFail() {
        TrainerDto createdTrainerDto = trainerFacade.create(null);
        assertNull(createdTrainerDto);
    }

    @Test
    void createTrainerNotValidFail() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDtoNotValid);
        assertNull(createdTrainerDto);
    }

    @Test
    void authenticateSuccessfully() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        Trainer trainer = trainerRepository.findByUserName(userNameForTrainer).get();
        boolean result = trainerFacade.authenticate(userNameForTrainer, trainer.getUser().getPassword());
        assertTrue(result, "authentication was successful");
    }

    @Test
    void authenticateNotSuccessfulWrongUserName() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        Trainer trainer = trainerRepository.findByUserName(userNameForTrainer).get();
        boolean result = trainerFacade.authenticate("AAAAAA", trainer.getUser().getPassword());

        assertFalse(result, "authentication with wrong userName not successful");
    }

    @Test
    void authenticateNotSuccessfulWrongPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        boolean result = trainerFacade.authenticate(userNameForTrainer, "AAAAAAAAA");

        assertFalse(result, "authentication with wrong password not successful");
    }

    @Test
    void authenticateNotSuccessfulNullUserName() {
        boolean result = trainerFacade.authenticate(null, "AAAAAA");
        assertFalse(result, "authentication with null username not successful");
    }

    @Test
    void authenticateNotSuccessfulNullPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        boolean result = trainerFacade.authenticate(userNameForTrainer, null);

        assertFalse(result, "authentication with null password not successful");
    }

    @Test
    void authenticateNotSuccessfulNullUserNameAndPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        boolean result = trainerFacade.authenticate(null, null);

        assertFalse(result, "authentication with null username and password not successful");
    }

    @Test
    void selectTrainerSuccessfully() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        String password = trainerRepository.findByUserName(userNameForTrainer).get().getUser().getPassword();
        TrainerDto selectedTrainerDto = trainerFacade.select(userNameForTrainer, password);

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());
        assertNotNull(selectedTrainerDto);
        assertNotNull(selectedTrainerDto);
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

        Trainer selectedTrainer = trainerRepository.findByUserName(selectedTrainerDto.getUser().getUserName()).get();
        assertNotNull(selectedTrainer);
        assertNotNull(selectedTrainer.getUser());
        assertAll(
                "Grouped assertions of created trainer",
                () -> assertEquals("Maria", selectedTrainer.getUser().getFirstName(),
                        "firstName should be equal"),
                () -> assertEquals("Petrenko", selectedTrainer.getUser().getLastName(),
                        "lastName should be equal"),
                () -> assertEquals(trainerDto.getUser().getIsActive(), selectedTrainer.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(trainerDto.getSpecialization().getTrainingTypeName(),
                        selectedTrainer.getSpecialization().getTrainingTypeName(),
                        "specialization should be equal")
        );
    }

    @Test
    void updateTrainerSuccessfully() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        String password = trainerRepository.findByUserName(userNameForTrainer).get().getUser().getPassword();
        TrainerDto updatedTrainerDto = trainerFacade.update(userNameForTrainer, password, trainerDto2);
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
                () -> assertEquals(trainerDto.getUser().getIsActive(), updatedTrainer.getUser().getIsActive(),
                        "isActive should be equal"),
                () -> assertEquals(trainerDto.getSpecialization().getTrainingTypeName(),
                        updatedTrainer.getSpecialization().getTrainingTypeName(),
                        "specialization should be equal")
        );
    }

    @Test
    void changeStatusSuccessfully() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        String password = trainerRepository.findByUserName(userNameForTrainer).get().getUser().getPassword();

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());

        boolean oldStatus = createdTrainerDto.getUser().getIsActive();
        boolean newStatus = !oldStatus;
        TrainerDto changedTrainerDto = trainerFacade.changeStatus(userNameForTrainer, password, newStatus);

        assertNotNull(changedTrainerDto);
        assertNotNull(changedTrainerDto.getUser());
        assertEquals(newStatus, changedTrainerDto.getUser().getIsActive());
    }

    @Test
    void changeStatusTheSecondTimeDoesntChange() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        String password = trainerRepository.findByUserName(userNameForTrainer).get().getUser().getPassword();

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());

        boolean oldStatus = createdTrainerDto.getUser().getIsActive();
        boolean newStatus = !oldStatus;
        TrainerDto changedTrainerDto = trainerFacade.changeStatus(userNameForTrainer, password, newStatus);

        assertNotNull(changedTrainerDto);
        assertNotNull(changedTrainerDto.getUser());
        assertEquals(newStatus, changedTrainerDto.getUser().getIsActive());

        TrainerDto changedagainTrainerDto = trainerFacade.changeStatus(userNameForTrainer, password, newStatus);

        assertNotNull(changedTrainerDto);
        assertNotNull(changedTrainerDto.getUser());
        assertEquals(newStatus, changedagainTrainerDto.getUser().getIsActive());
    }

    @Test
    void changePasswordSuccessfully() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        String password = trainerRepository.findByUserName(userNameForTrainer).get().getUser().getPassword();

        assertNotNull(createdTrainerDto);
        assertNotNull(createdTrainerDto.getUser());

        String newPassword = "1111111111";

        TrainerDto changedTrainerDto = trainerFacade.changePassword(userNameForTrainer, password, newPassword);
        String changedPassword = trainerRepository.findByUserName(userNameForTrainer).get().getUser().getPassword();

        assertNotNull(changedTrainerDto);
        assertNotNull(changedTrainerDto.getUser());
        assertEquals(newPassword, changedPassword);
    }

    @Test
    void selectNotSuccessfulWrongUserName() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        Trainer trainer = trainerRepository.findByUserName(userNameForTrainer).get();
        TrainerDto result = trainerFacade.select("AAAAAA", trainer.getUser().getPassword());

        assertNull(result, "select with wrong userName not successful");
    }

    @Test
    void selectNotSuccessfulWrongPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.select(userNameForTrainer, "AAAAAAAAA");

        assertNull(result, "select with wrong password not successful");
    }

    @Test
    void selectNotSuccessfulNullUserName() {
        TrainerDto result = trainerFacade.select(null, "AAAAAA");
        assertNull(result, "select with null username not successful");
    }

    @Test
    void selectNotSuccessfulNullPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.select(userNameForTrainer, null);

        assertNull(result, "select with null password not successful");
    }

    @Test
    void selectNotSuccessfulNullUserNameAndPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.select(null, null);

        assertNull(result, "select with null username and password not successful");
    }


    @Test
    void updateNotSuccessfulWrongUserName() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        System.out.println("368: " + userNameForTrainer);
        Trainer trainer = trainerRepository.findByUserName(userNameForTrainer).orElse(null);
        assert trainer != null;
        TrainerDto result = trainerFacade.update("AAAAAA", trainer.getUser().getPassword(), trainerDto);

        assertNull(result, "update with wrong userName not successful");
    }

    @Test
    void updateNotSuccessfulWrongPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.update(userNameForTrainer, "AAAAAAAAA", trainerDto);

        assertNull(result, "update with wrong password not successful");
    }

    @Test
    void updateNotSuccessfulNullUserName() {
        TrainerDto result = trainerFacade.update(null, "AAAAAA", trainerDto);
        assertNull(result, "update with null username not successful");
    }

    @Test
    void updateNotSuccessfulNullPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.update(userNameForTrainer, null, trainerDto);

        assertNull(result, "update with null password not successful");
    }

    @Test
    void updateNotSuccessfulNullUserNameAndPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.update(null, null, trainerDto);

        assertNull(result, "update with null username and password not successful");
    }

    @Test
    void changeStatusNotSuccessfulWrongUserName() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        Trainer trainer = trainerRepository.findByUserName(userNameForTrainer).get();
        TrainerDto result = trainerFacade.changeStatus(
                "AAAAAA", trainer.getUser().getPassword(), true);

        assertNull(result, "changeStatus with wrong userName not successful");
    }

    @Test
    void changeStatusNotSuccessfulWrongPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.changeStatus(userNameForTrainer, "AAAAAAAAA", true);

        assertNull(result, "changeStatus with wrong password not successful");
    }

    @Test
    void changeStatusNotSuccessfulNullUserName() {
        TrainerDto result = trainerFacade.changeStatus(null, "AAAAAA", true);
        assertNull(result, "changeStatus with null username not successful");
    }

    @Test
    void changeStatusNotSuccessfulNullPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.changeStatus(userNameForTrainer, null, true);

        assertNull(result, "changeStatus with null password not successful");
    }

    @Test
    void changeStatusNotSuccessfulNullUserNameAndPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.changeStatus(null, null, true);

        assertNull(result, "changeStatus with null username and password not successful");
    }

    @Test
    void changePasswordNotSuccessfulWrongUserName() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        Trainer trainer = trainerRepository.findByUserName(userNameForTrainer).get();
        TrainerDto result = trainerFacade.changePassword(
                "AAAAAA", trainer.getUser().getPassword(), "BBBBBB");

        assertNull(result, "changePassword with wrong userName not successful");
    }

    @Test
    void changePasswordNotSuccessfulWrongPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.changePassword(
                userNameForTrainer, "AAAAAAAAA", "BBBBBB");

        assertNull(result, "changePassword with wrong password not successful");
    }

    @Test
    void changePasswordNotSuccessfulNullUserName() {
        TrainerDto result = trainerFacade.changePassword(null, "AAAAAA", "BBBBBB");
        assertNull(result, "changePassword with null username not successful");
    }

    @Test
    void changePasswordNotSuccessfulNullPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.changePassword(userNameForTrainer, null, "BBBBBB");

        assertNull(result, "changePassword with null password not successful");
    }

    @Test
    void changePasswordNotSuccessfulNullUserNameAndPassword() {
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDto);
        userNameForTrainer = createdTrainerDto.getUser().getUserName();
        TrainerDto result = trainerFacade.changePassword(null, null, "BBBBBB");

        assertNull(result, "changePassword with null username and password not successful");
    }
}
