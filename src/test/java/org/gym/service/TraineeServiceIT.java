package org.gym.service;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@jakarta.transaction.Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class TraineeServiceIT {
    
    @Autowired
    private TraineeService traineeService;

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
        TraineeDto createdTraineeDto = traineeService.create(ds.traineeDto);
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
        TraineeDto createdTraineeDto = traineeService.create(ds.traineeDto);
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
                () -> assertEquals("Vinnitsya, Soborna str. 35, ap. 26", selectedTraineeDto.getAddress(), "address should be Vinnitsya, Soborna str. 35, ap. 26")
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
        TraineeDto createdTraineeDto = traineeService.create(ds.traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();
        TraineeDto updatedTraineeDto = traineeService.update(userNameForTrainee, ds.traineeDto2);
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
        TraineeDto createdTraineeDto = traineeService.create(ds.traineeDto);
        userNameForTrainee = createdTraineeDto.getUser().getUserName();

        assertNotNull(createdTraineeDto);
        assertNotNull(createdTraineeDto.getUser());
        traineeService.delete(userNameForTrainee);
        assertDoesNotThrow(() -> traineeRepository.findByUserName(userNameForTrainee));
    }

    @Test
    void changeStatusSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.create(ds.traineeDto);
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
        TraineeDto createdTraineeDto = traineeService.create(ds.traineeDto);
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
        TraineeDto createdTraineeDto = traineeService.create(ds.traineeDto);
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

        List<TrainerDto> updatedTrainersLists
                = traineeService.updateTrainersList(ds.traineeUserName, trainersList);

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
