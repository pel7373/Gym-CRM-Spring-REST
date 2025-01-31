package org.gym.service;

import lombok.extern.slf4j.Slf4j;
import org.gym.config.Config;
import org.gym.dto.*;
import org.gym.entity.*;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainingMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@jakarta.transaction.Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainingServiceIT {

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
    private TrainingMapper trainingMapper;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingDto trainingDto;
    private Training training;
    private TrainingType trainingType;
    private final String trainingTypeName = "Zumba";

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
        TrainingDto createdTrainingDto = trainingService.create(trainingDto);

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
        trainingService.create(trainingMapper.convertToDto(training));

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

        List<TrainingDto> trainings = trainingService.getTraineeTrainingsListCriteria(traineeTrainingsDto);

        assertTrue(trainings.isEmpty());
    }

    @Test
    void getByTraineeCriteriaSuccessfully() {
        trainingService.create(trainingMapper.convertToDto(training));

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

        List<TrainingDto> trainingsList = trainingService.getTraineeTrainingsListCriteria(traineeTrainingsDto);

        assertAll(
                () -> assertFalse(trainingsList.isEmpty()),
                () -> assertEquals(1, trainingsList.size()),
                () -> assertEquals("Maria.Petrenko", trainingsList.get(0).getTrainee().getUser().getUserName()),
                () -> assertEquals(trainingTypeName, trainingsList.get(0).getTrainingType().getTrainingTypeName())
        );
    }

    @Test
    void getByTraineeCriteriaNoResult() {
        trainingService.create(trainingMapper.convertToDto(training));

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

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.getTraineeTrainingsListCriteria(traineeTrainingsDto),
                "findByUserName: entity not found by userName NotValidUserName");
    }

    @Test
    void getByTrainerCriteriaNoResultAndException() {
        trainingService.create(trainingMapper.convertToDto(training));

        LocalDate fromDate = LocalDate.of(2035, 1, 1);
        LocalDate toDate = LocalDate.of(2036, 1, 1);
        String traineeName = trainee.getUser().getFirstName();

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName("NotValidTrainer")
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(traineeName)
                .build();

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto),
                "entity not found by userName NotValidTrainer");
    }

    @Test
    void getByTrainerCriteriaSuccessfully() {
        trainingService.create(trainingMapper.convertToDto(training));

        LocalDate fromDate = LocalDate.of(2020, 1, 1);
        LocalDate toDate = LocalDate.of(2040, 1, 1);
        String traineeUserName = trainee.getUser().getUserName();

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(traineeUserName)
                .build();

        List<TrainingDto> trainings = trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto);

        assertAll(
                () -> assertFalse(trainings.isEmpty()),
                () -> assertEquals(1, trainings.size()),
                () -> assertEquals(trainer.getUser().getUserName(), trainings.get(0).getTrainer().getUser().getUserName()),
                () -> assertEquals("Zumba", trainings.get(0).getTrainingType().getTrainingTypeName())
        );
    }

    @Test
    void getByTrainerCriteriaEmpty() {
        trainingService.create(trainingMapper.convertToDto(training));

        LocalDate fromDate = LocalDate.of(2050, 9, 8);
        LocalDate toDate = LocalDate.of(2060, 9, 8);
        String invalidTraineeName = "";

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(invalidTraineeName)
                .build();

        List<TrainingDto> trainings = trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto);

        assertTrue(trainings.isEmpty());
    }
}
