package org.gym.service.IT;

import org.gym.config.Config;
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
import org.gym.service.TrainingService;
import org.gym.service.UserNameGeneratorService;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@ActiveProfiles("prod")
@WebAppConfiguration
class TrainingServiceWithTestContainerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private UserNameGeneratorService userNameGeneratorService;

    @Autowired
    private PasswordGeneratorService passwordGeneratorService;

    @Autowired
    private TrainingTypeMapper trainingTypeMapper;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private final String trainingTypeName = "Zumba";
    private TrainingAddRequest trainingAddRequest;

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
        assertDoesNotThrow(() -> trainingService.create(trainingAddRequest));
    }

    @Test
    void getByTraineeCriteriaEmptyResult() {
        trainingService.create(trainingAddRequest);

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
                = trainingService.getTraineeTrainingsListCriteria(traineeTrainingsDto);

        assertTrue(traineeTrainingsListCriteria.isEmpty());
    }

    @Test
    void getByTraineeCriteriaSuccessfully() {
        trainingService.create(trainingAddRequest);

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
                = trainingService.getTraineeTrainingsListCriteria(traineeTrainingsDto);

        assertAll(
                () -> assertFalse(traineeTrainingsListCriteria.isEmpty()),
                () -> assertEquals(1, traineeTrainingsListCriteria.size()),
                () -> assertEquals(trainingTypeName, traineeTrainingsListCriteria.get(0).getTrainingType())
        );
    }

    @Test
    void getByTraineeCriteriaNoResult() {
        trainingService.create(trainingAddRequest);

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

        List<TraineeTrainingsListResponse> traineeTrainingsListCriteria = trainingService.getTraineeTrainingsListCriteria(traineeTrainingsDto);

        assertEquals(0, traineeTrainingsListCriteria.size());
    }

    @Test
    void getByTrainerCriteriaNoResultAndException() {
        trainingService.create(trainingAddRequest);

        LocalDate fromDate = LocalDate.of(2035, 1, 1);
        LocalDate toDate = LocalDate.of(2036, 1, 1);
        String traineeName = trainee.getUser().getFirstName();

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName("NotValidTrainer")
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(traineeName)
                .build();

        List<TrainerTrainingsListResponse> trainerTrainingsListCriteria = trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto);

        assertEquals(0, trainerTrainingsListCriteria.size());
    }

    @Test
    void getByTrainerCriteriaSuccessfully() {
        trainingService.create(trainingAddRequest);

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
                = trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto);

        assertAll(
                () -> assertFalse(trainerTrainingsListCriteria.isEmpty()),
                () -> assertEquals(1, trainerTrainingsListCriteria.size()),
                () -> assertEquals("Zumba",trainerTrainingsListCriteria.get(0).getTrainingType())
        );
    }

    @Test
    void getByTrainerCriteriaEmpty() {
        trainingService.create(trainingAddRequest);

        LocalDate fromDate = LocalDate.of(2050, 9, 8);
        LocalDate toDate = LocalDate.of(2060, 9, 8);
        String invalidTraineeName = "";

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(invalidTraineeName)
                .build();

        List<TrainerTrainingsListResponse> trainerTrainingsListCriteria = trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto);

        assertTrue(trainerTrainingsListCriteria.isEmpty());
    }
}
