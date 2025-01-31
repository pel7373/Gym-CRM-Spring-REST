package org.gym.repository;

import lombok.extern.slf4j.Slf4j;
import org.gym.config.Config;
import org.gym.dto.*;
import org.gym.entity.*;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainingMapper;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.UserNameGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainingRepositoryImplTest {

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingRepository trainingRepository;

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
    String trainingTypeName = "Zumba";

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
        Training createdTraining = trainingRepository.save(training);

        assertAll(
                "Grouped assertions of created trainerDto",
                () -> assertNotNull(createdTraining, "created training shouldn't be null"),
                () -> assertNotNull(createdTraining.getTrainer(), "created trainer shouldn't be null"),
                () -> assertNotNull(createdTraining.getTrainee(), "created trainee shouldn't be null"),
                () -> assertEquals(createdTraining.getTrainee().getUser().getFirstName(),
                        trainingDto.getTrainee().getUser().getFirstName(), "trainee's firstNames should be equal"),
                () -> assertEquals(createdTraining.getTrainer().getUser().getFirstName(),
                        trainingDto.getTrainer().getUser().getFirstName(), "trainer's firstNames should be equal"),
                () -> assertEquals(createdTraining.getTrainer().getSpecialization(),
                        training.getTrainer().getSpecialization(), "trainer's specialization should be equal"),
                () -> assertEquals(createdTraining.getTrainingName(),
                        trainingDto.getTrainingName(), "training names  should be equal"),
                () -> assertEquals(createdTraining.getDate(),
                        trainingDto.getDate(), "dates should be equal")
        );
    }

    @Test
    void getByTraineeCriteriaEmptyResult() {
        trainingRepository.save(training);

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

        List<Training> trainings = trainingRepository.getByTraineeCriteria(traineeTrainingsDto);

        assertTrue(trainings.isEmpty());
    }

    @Test
    void getByTraineeCriteriaSuccessfully() {
        trainingRepository.save(training);

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

        List<Training> trainingsList = trainingRepository.getByTraineeCriteria(traineeTrainingsDto);

        assertAll(
                () -> assertFalse(trainingsList.isEmpty()),
                () -> assertEquals(1, trainingsList.size()),
                () -> assertEquals("Maria.Petrenko", trainingsList.get(0).getTrainee().getUser().getUserName()),
                () -> assertEquals(trainingTypeName, trainingsList.get(0).getTrainingType().getTrainingTypeName())
        );
    }

    @Test
    void getByTraineeCriteriaNoResult() {
        trainingRepository.save(training);

        LocalDate fromDate = LocalDate.of(2010, 8, 1);
        LocalDate toDate = LocalDate.of(2040, 8, 1);
        String trainerUserName = trainer.getUser().getUserName();

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName("NotValidUserName")
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerUserName(trainerUserName)
                .trainingType("Yoga")
                .build();

        List<Training> trainings = trainingRepository.getByTraineeCriteria(traineeTrainingsDto);

        assertTrue(trainings.isEmpty());
    }

    @Test
    void getByTrainerCriteriaNoResult() {
        trainingRepository.save(training);

        LocalDate fromDate = LocalDate.of(2035, 1, 1);
        LocalDate toDate = LocalDate.of(2036, 1, 1);
        String traineeUserName = trainee.getUser().getUserName();

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName("NotValidTrainer")
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(traineeUserName)
                .build();

        List<Training> trainings = trainingRepository.getByTrainerCriteria(trainerTrainingsDto);

        assertTrue(trainings.isEmpty());
    }

    @Test
    void getByTrainerCriteriaSuccessfully() {
        trainingRepository.save(training);

        LocalDate fromDate = LocalDate.of(2020, 1, 1);
        LocalDate toDate = LocalDate.of(2040, 1, 1);
        String traineeUserName = trainee.getUser().getUserName();

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(traineeUserName)
                .build();

        List<Training> trainings = trainingRepository.getByTrainerCriteria(trainerTrainingsDto);

        assertAll(
                () -> assertFalse(trainings.isEmpty()),
                () -> assertEquals(1, trainings.size()),
                () -> assertEquals(trainer.getUser().getUserName(), trainings.get(0).getTrainer().getUser().getUserName()),
                () -> assertEquals("Zumba", trainings.get(0).getTrainingType().getTrainingTypeName())
        );
    }

    @Test
    void getByTrainerCriteriaEmpty() {
        trainingRepository.save(training);

        LocalDate fromDate = LocalDate.of(2050, 9, 8);
        LocalDate toDate = LocalDate.of(2060, 9, 8);
        String invalidTraineeName = "";

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(trainer.getUser().getUserName())
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName(invalidTraineeName)
                .build();

        List<Training> trainings = trainingRepository.getByTrainerCriteria(trainerTrainingsDto);

        assertTrue(trainings.isEmpty());
    }
}
