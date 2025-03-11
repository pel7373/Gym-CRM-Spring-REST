package org.gym.controller.IT;

import org.gym.config.Config;
import org.gym.config.TestConfig;
import org.gym.controller.TrainingController;
import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;
import org.gym.entity.*;
import org.gym.mapper.TrainingTypeMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.UserNameGeneratorService;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
@ActiveProfiles("test")
class TrainingControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrainingController trainingController;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private TrainingTypeMapper trainingTypeMapper;

    @Autowired
    private UserNameGeneratorService userNameGeneratorService;

    @Autowired
    private PasswordGeneratorService passwordGeneratorService;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private final String trainingTypeName = "Zumba";
    private TrainingAddRequest trainingAddRequest;

    @BeforeEach
    void setUp()
    {
        trainingType = trainingTypeRepository.findByName(trainingTypeName).get();

        User userForTrainee = User.builder()
                .firstName("Maria")
                .lastName("Petrenko")
                .userName(userNameGeneratorService.generate("Maria", "Petrenko"))
                .password(passwordGeneratorService.generate())
                .isActive(true)
                .build();
        trainee = Trainee.builder()
                .user(userForTrainee)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        User userForTrainer = User.builder()
                .firstName("Petro")
                .lastName("Ivanenko")
                .userName(userNameGeneratorService.generate("Petro", "Ivanenko"))
                .password(passwordGeneratorService.generate())
                .isActive(true)
                .build();
        trainer = Trainer.builder()
                .user(userForTrainer)
                .specialization(trainingType)
                .build();

        Trainee createdTrainee = traineeRepository.save(trainee);
        Trainer createdTrainer = trainerRepository.save(trainer);

        Training training = Training.builder()
                .trainingType(trainingType)
                .trainer(createdTrainer)
                .trainee(createdTrainee)
                .trainingName("Zumba next workout")
                .trainingType(trainingType)
                .date(LocalDate.now().plusDays(3))
                .duration(45)
                .build();

        trainingAddRequest = TrainingAddRequest.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .traineeUserName(trainee.getUser().getUserName())
                .trainingType(trainingTypeMapper.convertToDto(trainingType))
                .trainingName(training.getTrainingName())
                .date(training.getDate())
                .duration(training.getDuration())
                .build();
    }

    @Test
    void createTrainingSuccessfully() {
        assertDoesNotThrow(() -> trainingController.addTraining(trainingAddRequest));
    }

    @Test
    void getByTraineeCriteriaEmptyResult() {
        trainingController.addTraining(trainingAddRequest);

        LocalDate fromDate = LocalDate.of(2030, 3, 5);
        LocalDate toDate = LocalDate.of(2050, 3, 5);
        String differentTrainerName = "";

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName(trainee.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerUserName(differentTrainerName)
                .trainingType("stretching")
                .build();

        List<TraineeTrainingsListResponse> traineeTrainingsListCriteria
                = trainingController.getTraineeTrainings(traineeTrainingsDto);

        assertTrue(traineeTrainingsListCriteria.isEmpty());
    }

    @Test
    void getByTraineeCriteriaSuccessfully() {
        trainingController.addTraining(trainingAddRequest);

        LocalDate fromDate = LocalDate.of(2010, 2, 9);
        LocalDate toDate = LocalDate.of(2035, 3, 9);
        String trainerUserName = trainer.getUser().getUserName();
        trainingType = TrainingType.builder().trainingTypeName(trainingTypeName).build();

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName(trainee.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerUserName(trainerUserName)
                .trainingType(trainingTypeName)
                .build();

        List<TraineeTrainingsListResponse> traineeTrainingsListCriteria
                = trainingController.getTraineeTrainings(traineeTrainingsDto);

        assertAll(
                () -> assertFalse(traineeTrainingsListCriteria.isEmpty()),
                () -> assertEquals(1, traineeTrainingsListCriteria.size()),
                () -> assertEquals(trainingTypeName, traineeTrainingsListCriteria.get(0).getTrainingType())
        );
    }

    @Test
    void getByTraineeCriteriaWithoutCriteriaSuccessfully() {
        trainingController.addTraining(trainingAddRequest);

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName(trainee.getUser().getUserName())
                .build();

        List<TraineeTrainingsListResponse> traineeTrainingsListCriteria
                = trainingController.getTraineeTrainings(traineeTrainingsDto);

        assertAll(
                () -> assertFalse(traineeTrainingsListCriteria.isEmpty()),
                () -> assertEquals(1, traineeTrainingsListCriteria.size()),
                () -> assertEquals(trainingTypeName, traineeTrainingsListCriteria.get(0).getTrainingType())
        );
    }

    @Test
    void getByTraineeCriteriaNoResult() {
        trainingController.addTraining(trainingAddRequest);

        LocalDate fromDate = LocalDate.of(2010, 8, 1);
        LocalDate toDate = LocalDate.of(2040, 8, 1);
        String trainerUserName = trainer.getUser().getFirstName();

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName("NotValidUserName")
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerUserName(trainerUserName)
                .trainingType("Roga")
                .build();

        List<TraineeTrainingsListResponse> traineeTrainingsListCriteria =
                trainingController.getTraineeTrainings(traineeTrainingsDto);

        assertEquals(0, traineeTrainingsListCriteria.size());
    }

    @Test
    void getByTrainerCriteriaNoResultAndException() {
        trainingController.addTraining(trainingAddRequest);

        LocalDate fromDate = LocalDate.of(2035, 1, 1);
        LocalDate toDate = LocalDate.of(2036, 1, 1);
        String traineeName = trainee.getUser().getFirstName();

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName("NotValidTrainer")
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(traineeName)
                .build();

        List<TrainerTrainingsListResponse> trainerTrainingsListCriteria =
                trainingController.getTrainerTrainings(trainerTrainingsDto);

        assertEquals(0, trainerTrainingsListCriteria.size());
    }

    @Test
    void getByTrainerCriteriaSuccessfully() {
        trainingController.addTraining(trainingAddRequest);

        LocalDate fromDate = LocalDate.of(2020, 1, 1);
        LocalDate toDate = LocalDate.of(2040, 1, 1);
        String traineeUserName = trainee.getUser().getUserName();

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(traineeUserName)
                .build();

        List<TrainerTrainingsListResponse> trainerTrainingsListCriteria
                = trainingController.getTrainerTrainings(trainerTrainingsDto);

        assertAll(
                () -> assertFalse(trainerTrainingsListCriteria.isEmpty()),
                () -> assertEquals(1, trainerTrainingsListCriteria.size()),
                () -> assertEquals("Zumba",trainerTrainingsListCriteria.get(0).getTrainingType())
        );
    }

    @Test
    void getByTrainerCriteriaWithoutCriteriaSuccessfully() {
        trainingController.addTraining(trainingAddRequest);

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .build();

        List<TrainerTrainingsListResponse> trainerTrainingsListCriteria
                = trainingController.getTrainerTrainings(trainerTrainingsDto);

        assertAll(
                () -> assertFalse(trainerTrainingsListCriteria.isEmpty()),
                () -> assertEquals(1, trainerTrainingsListCriteria.size()),
                () -> assertEquals("Zumba",trainerTrainingsListCriteria.get(0).getTrainingType())
        );
    }

    @Test
    void getByTrainerCriteriaEmpty() {
        trainingController.addTraining(trainingAddRequest);

        LocalDate fromDate = LocalDate.of(2050, 9, 8);
        LocalDate toDate = LocalDate.of(2060, 9, 8);
        String invalidTraineeName = "";

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(invalidTraineeName)
                .build();

        List<TrainerTrainingsListResponse> trainerTrainingsListCriteria =
                trainingController.getTrainerTrainings(trainerTrainingsDto);

        assertTrue(trainerTrainingsListCriteria.isEmpty());
    }
}
