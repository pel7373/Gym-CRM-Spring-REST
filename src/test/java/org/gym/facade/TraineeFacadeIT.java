package org.gym.facade;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.dto.TrainerDto;
import org.gym.dto.UserDto;
import org.gym.entity.Trainee;
import org.gym.dto.TraineeDto;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.facade.impl.TraineeFacadeImpl;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.TraineeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@jakarta.transaction.Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class TraineeFacadeIT {

    @Autowired
    private TraineeFacadeImpl traineeFacade;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    private final TraineeDto traineeDto;
    private final TraineeDto traineeDto2;
    private final TraineeDto traineeDtoNotValid;
    private String userNameForTrainee;
    private final DataStorage ds = new DataStorage();

    {
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        UserDto userDto2 = new UserDto("Petro", "Ivanenko", "Petro.Ivanenko", true);
        UserDto userDtoNotValid = new UserDto("Pa", "Pa", "Maria.Petrenko2", false);

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

        traineeDtoNotValid
                = new TraineeDto(userDtoNotValid, LocalDate.of(1995, 1, 23),
                "Kyiv, Soborna str. 35, ap. 26");
    }

    @AfterEach
    void destroy() {
        traineeService.delete(userNameForTrainee);
    }

    @Test
    void createTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals("Maria", createdTraineeDto.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals("Petrenko", createdTraineeDto.getUser().getLastName(), "lastName should be Petrenko"),
                () -> assertTrue(createdTraineeDto.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Vinnitsya, Soborna str. 35, ap. 26", createdTraineeDto.getAddress(), "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );

        Trainee createdTrainee = traineeRepository.findByUserName(createdTraineeDto.getUser().getUserName()).get();
        assertNotNull(createdTrainee);
        assertNotNull(createdTrainee.getUser());
        assertAll(
                "Grouped assertions of created trainee",
                () -> assertEquals("Maria", createdTrainee.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals("Petrenko", createdTrainee.getUser().getLastName(), "lastName should be Petrenko"),
                () -> assertTrue(createdTrainee.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Vinnitsya, Soborna str. 35, ap. 26", createdTrainee.getAddress(), "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );
    }

    @Test
    void createTraineeNullFail() {
        TraineeDto createdTraineeDto = traineeFacade.create(null);
        assertNull(createdTraineeDto);
   }

    @Test
    void createTraineeNotValidFail() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDtoNotValid);
        assertNull(createdTraineeDto);
    }

    @Test
    void authenticateSuccessfully() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        Trainee trainee = traineeRepository.findByUserName(userNameForTrainee).get();
        boolean result = traineeFacade.authenticate(userNameForTrainee, trainee.getUser().getPassword());
        assertTrue(result, "authentication was successful");
    }

    @Test
    void authenticateNotSuccessfulWrongUserName() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        Trainee trainee = traineeRepository.findByUserName(userNameForTrainee).get();
        boolean result = traineeFacade.authenticate("AAAAAA", trainee.getUser().getPassword());

        assertFalse(result, "authentication with wrong userName not successful");
    }

    @Test
    void authenticateNotSuccessfulWrongPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        boolean result = traineeFacade.authenticate(userNameForTrainee, "AAAAAAAAA");

        assertFalse(result, "authentication with wrong password not successful");
    }

    @Test
    void authenticateNotSuccessfulNullUserName() {
        boolean result = traineeFacade.authenticate(null, "AAAAAA");
        assertFalse(result, "authentication with null username not successful");
    }

    @Test
    void authenticateNotSuccessfulNullPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        boolean result = traineeFacade.authenticate(userNameForTrainee, null);

        assertFalse(result, "authentication with null password not successful");
    }

    @Test
    void authenticateNotSuccessfulNullUserNameAndPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        boolean result = traineeFacade.authenticate(null, null);

        assertFalse(result, "authentication with null username and password not successful");
    }

    @Test
    void selectTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        String password = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();
        TraineeDto selectedTraineeDto = traineeFacade.select(userNameForTrainee, password);

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());
        assertNotNull(selectedTraineeDto);
        assertNotNull(selectedTraineeDto);
        assertAll(
                "Grouped assertions of selected traineeDto",
                () -> assertEquals("Maria", selectedTraineeDto.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals("Petrenko", selectedTraineeDto.getUser().getLastName(), "lastName should be Petrenko"),
                () -> assertTrue(selectedTraineeDto.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Vinnitsya, Soborna str. 35, ap. 26", selectedTraineeDto.getAddress(), "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );

        Trainee selectedTrainee = traineeRepository.findByUserName(selectedTraineeDto.getUser().getUserName()).get();
        assertNotNull(selectedTrainee);
        assertNotNull(selectedTrainee.getUser());
        assertAll(
                "Grouped assertions of created trainee",
                () -> assertEquals("Maria", selectedTrainee.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals("Petrenko", selectedTrainee.getUser().getLastName(), "lastName should be Petrenko"),
                () -> assertTrue(selectedTrainee.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Vinnitsya, Soborna str. 35, ap. 26", selectedTrainee.getAddress(), "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );
    }

    @Test
    void updateTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        String password = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();
        TraineeDto updatedTraineeDto = traineeFacade.update(userNameForTrainee, password, traineeDto2);
        userNameForTrainee = updatedTraineeDto.getUser().getUserName();

        assertNotNull(updatedTraineeDto);
        assertNotNull(updatedTraineeDto.getUser());
        assertAll(
                "Grouped assertions of selected traineeDto",
                () -> assertEquals("Petro", updatedTraineeDto.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals("Ivanenko", updatedTraineeDto.getUser().getLastName(), "lastName should be Petrenko"),
                () -> assertTrue(updatedTraineeDto.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Kyiv, Soborna str. 35, ap. 26", updatedTraineeDto.getAddress(), "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );

        Trainee updatedTrainee = traineeRepository.findByUserName(updatedTraineeDto.getUser().getUserName()).get();
        assertNotNull(updatedTrainee);
        assertNotNull(updatedTrainee.getUser());
        assertAll(
                "Grouped assertions of created trainee",
                () -> assertEquals("Petro", updatedTrainee.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals("Ivanenko", updatedTrainee.getUser().getLastName(), "lastName should be Petrenko"),
                () -> assertTrue(updatedTrainee.getUser().getIsActive(), "isActive should be true"),
                () -> assertEquals("Kyiv, Soborna str. 35, ap. 26", updatedTrainee.getAddress(), "address should be Vinnitsya, Soborna str. 35, ap. 26")
        );
    }

    @Test
    void deleteTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        String password = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());
        traineeFacade.delete(userNameForTrainee, password);
    }

    @Test
    void changeStatusSuccessfully() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        String password = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());

        boolean oldStatus = createdTraineeDto.getUser().getIsActive();
        boolean newStatus = !oldStatus;
        TraineeDto changedTraineeDto = traineeFacade.changeStatus(userNameForTrainee, password, newStatus);

        assertNotNull(changedTraineeDto);
        assertNotNull(changedTraineeDto.getUser());
        assertEquals(newStatus, changedTraineeDto.getUser().getIsActive());
    }

    @Test
    void changeStatusTheSecondTimeDoesntChange() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        String password = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());

        boolean oldStatus = createdTraineeDto.getUser().getIsActive();
        boolean newStatus = !oldStatus;
        TraineeDto changedTraineeDto = traineeFacade.changeStatus(userNameForTrainee, password, newStatus);

        assertNotNull(changedTraineeDto);
        assertNotNull(changedTraineeDto.getUser());
        assertEquals(newStatus, changedTraineeDto.getUser().getIsActive());

        TraineeDto changedagainTraineeDto = traineeFacade.changeStatus(userNameForTrainee, password, newStatus);

        assertNotNull(changedTraineeDto);
        assertNotNull(changedTraineeDto.getUser());
        assertEquals(newStatus, changedagainTraineeDto.getUser().getIsActive());
    }

    @Test
    void changePasswordSuccessfully() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        String password = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());

        String newPassword = "1111111111";

        TraineeDto changedTraineeDto = traineeFacade.changePassword(userNameForTrainee, password, newPassword);
        String changedPassword = traineeRepository.findByUserName(userNameForTrainee).get().getUser().getPassword();

        assertNotNull(changedTraineeDto);
        assertNotNull(changedTraineeDto.getUser());
        assertEquals(newPassword, changedPassword);
    }

    @Test
    void selectNotSuccessfulWrongUserName() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        Trainee trainee = traineeRepository.findByUserName(userNameForTrainee).get();
        TraineeDto result = traineeFacade.select("AAAAAA", trainee.getUser().getPassword());

        assertNull(result, "select with wrong userName not successful");
    }

    @Test
    void selectNotSuccessfulWrongPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.select(userNameForTrainee, "AAAAAAAAA");

        assertNull(result, "select with wrong password not successful");
    }

    @Test
    void selectNotSuccessfulNullUserName() {
        TraineeDto result = traineeFacade.select(null, "AAAAAA");
        assertNull(result, "select with null username not successful");
    }

    @Test
    void selectNotSuccessfulNullPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.select(userNameForTrainee, null);

        assertNull(result, "select with null password not successful");
    }

    @Test
    void selectNotSuccessfulNullUserNameAndPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.select(null, null);

        assertNull(result, "select with null username and password not successful");
    }


    @Test
    void updateNotSuccessfulWrongUserName() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        System.out.println("368: " + userNameForTrainee);
        Trainee trainee = traineeRepository.findByUserName(userNameForTrainee).orElse(null);
        TraineeDto result = traineeFacade.update("AAAAAA", trainee.getUser().getPassword(), traineeDto);

        assertNull(result, "update with wrong userName not successful");
    }

    @Test
    void updateNotSuccessfulWrongPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.update(userNameForTrainee, "AAAAAAAAA", traineeDto);

        assertNull(result, "update with wrong password not successful");
    }

    @Test
    void updateNotSuccessfulNullUserName() {
        TraineeDto result = traineeFacade.update(null, "AAAAAA", traineeDto);
        assertNull(result, "update with null username not successful");
    }

    @Test
    void updateNotSuccessfulNullPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.update(userNameForTrainee, null, traineeDto);

        assertNull(result, "update with null password not successful");
    }

    @Test
    void updateNotSuccessfulNullUserNameAndPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.update(null, null, traineeDto);

        assertNull(result, "update with null username and password not successful");
    }

    @Test
    void changeStatusNotSuccessfulWrongUserName() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        Trainee trainee = traineeRepository.findByUserName(userNameForTrainee).get();
        TraineeDto result = traineeFacade.changeStatus("AAAAAA", trainee.getUser().getPassword(), true);

        assertNull(result, "changeStatus with wrong userName not successful");
    }

    @Test
    void changeStatusNotSuccessfulWrongPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.changeStatus(userNameForTrainee, "AAAAAAAAA", true);

        assertNull(result, "changeStatus with wrong password not successful");
    }

    @Test
    void changeStatusNotSuccessfulNullUserName() {
        TraineeDto result = traineeFacade.changeStatus(null, "AAAAAA", true);
        assertNull(result, "changeStatus with null username not successful");
    }

    @Test
    void changeStatusNotSuccessfulNullPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.changeStatus(userNameForTrainee, null, true);

        assertNull(result, "changeStatus with null password not successful");
    }

    @Test
    void changeStatusNotSuccessfulNullUserNameAndPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.changeStatus(null, null, true);

        assertNull(result, "changeStatus with null username and password not successful");
    }

    @Test
    void changePasswordNotSuccessfulWrongUserName() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        Trainee trainee = traineeRepository.findByUserName(userNameForTrainee).get();
        TraineeDto result = traineeFacade.changePassword("AAAAAA", trainee.getUser().getPassword(), "BBBBBB");

        assertNull(result, "changePassword with wrong userName not successful");
    }

    @Test
    void changePasswordNotSuccessfulWrongPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.changePassword(userNameForTrainee, "AAAAAAAAA", "BBBBBB");

        assertNull(result, "changePassword with wrong password not successful");
    }

    @Test
    void changePasswordNotSuccessfulNullUserName() {
        TraineeDto result = traineeFacade.changePassword(null, "AAAAAA", "BBBBBB");
        assertNull(result, "changePassword with null username not successful");
    }

    @Test
    void changePasswordNotSuccessfulNullPassword() {
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto result = traineeFacade.changePassword(userNameForTrainee, null, "BBBBBB");

        assertNull(result, "changePassword with null password not successful");
    }

    @Test
    void changePasswordNotSuccessfulNullUserNameAndPassword() {
        TraineeDto result = traineeFacade.changePassword(null, null, "BBBBBB");

        assertNull(result, "changePassword with null username and password not successful");
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
        String password = traineeRepository.findByUserName(ds.traineeUserName).get().getUser().getPassword();
        createdTrainee1.setTrainers(List.of(createdTrainer1));

        List<TrainerDto> unassignedTrainers = traineeFacade.getUnassignedTrainers(ds.traineeUserName, password);

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
        String password = traineeRepository.findByUserName(ds.traineeUserName).get().getUser().getPassword();

        List<TrainerDto> unassignedTrainers = traineeFacade.getUnassignedTrainers(ds.traineeUserName, password);

        assertAll(
                "Grouped assertions of getUnassigned trainersDto's list",
                () -> assertNotNull(unassignedTrainers),
                () -> assertEquals(0, unassignedTrainers.size())
        );
    }

    @Test
    void updateTrainersListSuccessfully() {
        String trainingTypeNameTrainer = "Zumba";
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeNameTrainer).get();
        ds.trainer1.setSpecialization(trainingType);
        ds.trainer2.setSpecialization(trainingType);

        traineeRepository.save(ds.trainee1);
        trainerRepository.save(ds.trainer1);
        trainerRepository.save(ds.trainer2);
        String password = traineeRepository.findByUserName(ds.traineeUserName).get().getUser().getPassword();

        List<String> trainersList = List.of(
                ds.trainer1.getUser().getUserName(),
                ds.trainer2.getUser().getUserName());

        List<TrainerDto> updatedTrainersLists
                = traineeFacade.updateTrainersList(ds.traineeUserName, password, trainersList);

        Trainee checkTrainee = traineeRepository.findByUserName(ds.traineeUserName).get();

        assertAll(
                "Grouped assertions of updateTrainersList successfully",
                () -> assertNotNull(checkTrainee.getTrainers()),
                () -> assertEquals(2, checkTrainee.getTrainers().size()),
                () -> assertNotNull(updatedTrainersLists),
                () -> assertEquals(2, updatedTrainersLists.size())
        );
    }
}
