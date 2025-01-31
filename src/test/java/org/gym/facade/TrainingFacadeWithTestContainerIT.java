package org.gym.facade;

import lombok.extern.slf4j.Slf4j;
import org.gym.config.Config;
import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.TrainingDto;
import org.gym.entity.*;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainingMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.UserNameGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@jakarta.transaction.Transactional
class TrainingFacadeWithTestContainerIT {

    @Autowired
    private TrainingFacade trainingFacade;

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
    private TrainingMapper trainingMapper;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingDto trainingDto;
    private Training training;
    private TrainingType trainingType;
    private final String trainingTypeName = "Zumba";

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

    @BeforeEach
    void setUp()
    {
        try {
            trainingType = trainingTypeRepository.findByName(trainingTypeName).get();
        } catch (EntityNotFoundException e) {
            LOGGER.warn("TrainingService: can't get trainingType {}", trainingTypeName);
        }

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

        training = Training.builder()
                .trainingType(trainingType)
                .trainer(createdTrainer)
                .trainee(createdTrainee)
                .trainingName("Zumba next workout")
                .trainingType(trainingType)
                .date(LocalDate.now().plusDays(3))
                .duration(45)
                .build();

        trainingDto = trainingMapper.convertToDto(training);
    }

    @Test
    void createTrainingSuccessfully() {
        TrainingDto createdTrainingDto = trainingFacade.create(trainingDto);
        assertNotNull(createdTrainingDto);

        assertAll(
                "Grouped assertions of created trainerDto",
                () -> assertNotNull(createdTrainingDto, "created training shouldn't be null"),
                () -> assertNotNull(createdTrainingDto.getTrainer(), "created trainer shouldn't be null"),
                () -> assertNotNull(createdTrainingDto.getTrainee(), "created trainee shouldn't be null"),
                () -> assertEquals(createdTrainingDto.getTrainee().getUser().getFirstName(),
                        trainingDto.getTrainee().getUser().getFirstName(),
                        "trainee's firstNames should be equal"),
                () -> assertEquals(createdTrainingDto.getTrainer().getUser().getFirstName(),
                        trainingDto.getTrainer().getUser().getFirstName(),
                        "trainer's firstNames should be equal"),
                () -> assertEquals(createdTrainingDto.getTrainer().getSpecialization(),
                        trainingDto.getTrainer().getSpecialization(),
                        "trainer's specialization should be equal"),
                () -> assertEquals(createdTrainingDto.getTrainingName(),
                        trainingDto.getTrainingName(), "training names  should be equal"),
                () -> assertEquals(createdTrainingDto.getDate(),
                        trainingDto.getDate(), "dates should be equal")
        );
    }

    @Test
    void getByTraineeCriteriaEmptyResult() {
        TrainingDto createdTrainingDto = trainingFacade.create(trainingMapper.convertToDto(training));
        assertNotNull(createdTrainingDto);

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

        List<TrainingDto> trainings = trainingFacade.getTraineeTrainings(traineeTrainingsDto);

        assertTrue(trainings.isEmpty());
    }

    @Test
    void getByTraineeCriteriaSuccessfully() {
        TrainingDto createdTrainingDto = trainingFacade.create(trainingMapper.convertToDto(training));
        assertNotNull(createdTrainingDto);

        LocalDate fromDate = LocalDate.of(2010, 2, 9);
        LocalDate toDate = LocalDate.of(2035, 3, 9);
        String trainerUserName = trainer.getUser().getUserName();

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName(trainee.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerUserName(trainerUserName)
                .trainingType(trainingTypeName)
                .build();

        List<TrainingDto> trainingsList = trainingFacade.getTraineeTrainings(traineeTrainingsDto);

        assertAll(
                () -> assertFalse(trainingsList.isEmpty()),
                () -> assertEquals(1, trainingsList.size()),
                () -> assertEquals("Maria.Petrenko",
                        trainingsList.get(0).getTrainee().getUser().getUserName()),
                () -> assertEquals(trainingTypeName, trainingsList.get(0).getTrainingType().getTrainingTypeName())
        );
    }

    @Test
    void getByTraineeCriteriaNoResult() {
        TrainingDto createdTrainingDto = trainingFacade.create(trainingMapper.convertToDto(training));
        assertNotNull(createdTrainingDto);

        LocalDate fromDate = LocalDate.of(2010, 8, 1);
        LocalDate toDate = LocalDate.of(2040, 8, 1);
        String trainerUserName = trainer.getUser().getUserName();

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName("NotValidUserName")
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerUserName(trainerUserName)
                .trainingType("Roga")
                .build();

        List<TrainingDto> traineeTrainings = trainingFacade.getTraineeTrainings(traineeTrainingsDto);
        assertNotNull(traineeTrainings);
        assertEquals(0, traineeTrainings.size());
    }
    @Test
    void getByTrainerCriteriaNoResultAndException() {
        TrainingDto createdTrainingDto = trainingFacade.create(trainingMapper.convertToDto(training));
        assertNotNull(createdTrainingDto);

        LocalDate fromDate = LocalDate.of(2035, 1, 1);
        LocalDate toDate = LocalDate.of(2036, 1, 1);
        String traineeName = trainee.getUser().getFirstName();

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName("NotValidTrainer")
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(traineeName)
                .build();

        List<TrainingDto> trainerTrainings = trainingFacade.getTrainerTrainings(trainerTrainingsDto);

        assertNotNull(trainerTrainings);
        assertEquals(0, trainerTrainings.size());
    }

    @Test
    void getByTrainerCriteriaSuccessfully() {
        TrainingDto createdTrainingDto = trainingFacade.create(trainingMapper.convertToDto(training));
        assertNotNull(createdTrainingDto);

        LocalDate fromDate = LocalDate.now().minusYears(10);
        LocalDate toDate = LocalDate.now().plusYears(10);
        String traineeUserName = trainee.getUser().getUserName();

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(traineeUserName)
                .build();

        List<TrainingDto> trainings = trainingFacade.getTrainerTrainings(trainerTrainingsDto);

        assertAll(
                () -> assertFalse(trainings.isEmpty()),
                () -> assertEquals(1, trainings.size()),
                () -> assertEquals(trainer.getUser().getUserName(), trainings.get(0).getTrainer().getUser().getUserName()),
                () -> assertEquals("Zumba", trainings.get(0).getTrainingType().getTrainingTypeName())
        );
    }

    @Test
    void getByTrainerCriteriaEmpty() {
        TrainingDto createdTrainingDto = trainingFacade.create(trainingMapper.convertToDto(training));
        assertNotNull(createdTrainingDto);

        LocalDate fromDate = LocalDate.of(2050, 9, 8);
        LocalDate toDate = LocalDate.of(2060, 9, 8);
        String invalidTraineeName = "";

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(invalidTraineeName)
                .build();

        List<TrainingDto> trainings = trainingFacade.getTrainerTrainings(trainerTrainingsDto);
        assertTrue(trainings.isEmpty());
    }
}
