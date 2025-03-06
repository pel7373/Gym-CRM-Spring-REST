package org.gym.service.Test;

import org.gym.dto.*;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.entity.*;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainingMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainingServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Trainee trainee;
    private TrainingType trainingType;
    private String trainingTypeName;
    private Training training;
    private Trainer trainer;
    private TrainingAddRequest trainingAddRequest;

    String trainerUserName = "Petro.Ivanenko";
    String traineeUserName = "Maria.Petrenko";

    @BeforeEach
    void setUp() {
        trainingTypeName = "Zumba";
        TrainingTypeDto trainingTypeDto = TrainingTypeDto.builder()
                .trainingTypeName(trainingTypeName)
                .build();

        trainingAddRequest = TrainingAddRequest.builder()
                .trainingType(trainingTypeDto)
                .trainerUserName(trainerUserName)
                .traineeUserName(traineeUserName)
                .trainingName("Zumba next workout")
                .trainingType(trainingTypeDto)
                .date(LocalDate.now().plusDays(3))
                .duration(45)
                .build();


        trainee = Trainee.builder()
                .user(new User(null, "Maria", "Petrenko", "Maria.Petrenko", "", true))
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        trainer = Trainer.builder()
                .user(new User(null, "Petro", "Ivanenko", "Petro.Ivanenko", "", true))
                .specialization(TrainingType.builder()
                        .trainingTypeName(trainingTypeName)
                        .build())
                .build();

        trainingType = TrainingType.builder()
                .trainingTypeName(trainingTypeName)
                .build();

        training = Training.builder()
                .trainingType(trainingType)
                .trainer(trainer)
                .trainee(trainee)
                .trainingName("Zumba next workout")
                .trainingType(trainingType)
                .date(LocalDate.now().plusDays(3))
                .duration(45)
                .build();
    }

    @Test
    void createTrainingSuccessfully() {
        when(trainingTypeRepository.findByName(trainingTypeName)).thenReturn(Optional.ofNullable(trainingType));
        when(trainerRepository.findByUserName(trainerUserName)).thenReturn(Optional.ofNullable(trainer));
        when(traineeRepository.findByUserName(traineeUserName)).thenReturn(Optional.ofNullable(trainee));
        when(trainingRepository.save(training)).thenReturn(training);

        trainingService.create(trainingAddRequest);

        verify(trainingTypeRepository, times(1)).findByName(trainingTypeName);
        verify(trainerRepository, times(1)).findByUserName(trainerUserName);
        verify(traineeRepository, times(1)).findByUserName(traineeUserName);
        verify(trainingRepository, times(1)).save(training);
    }

    @Test
    void createTrainingNotValidTrainingTypeNameFail() {
        when(trainingTypeRepository.findByName(trainingTypeName)).thenReturn(Optional.empty());
        when(trainerRepository.findByUserName(trainerUserName)).thenReturn(Optional.ofNullable(trainer));
        when(traineeRepository.findByUserName(traineeUserName)).thenReturn(Optional.ofNullable(trainee));
        when(trainingRepository.save(training)).thenReturn(training);

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(trainingAddRequest));

        verify(trainingTypeRepository, times(1)).findByName(trainingTypeName);
        verify(trainerRepository, times(0)).findByUserName(trainerUserName);
        verify(traineeRepository, times(0)).findByUserName(traineeUserName);
        verify(trainingRepository, times(0)).save(training);
    }

    @Test
    void createTrainingNotValidTrainerNameFail() {
        when(trainingTypeRepository.findByName(trainingTypeName)).thenReturn(Optional.ofNullable(trainingType));
        when(trainerRepository.findByUserName(trainerUserName)).thenReturn(Optional.empty());
        when(traineeRepository.findByUserName(traineeUserName)).thenReturn(Optional.ofNullable(trainee));
        when(trainingRepository.save(training)).thenReturn(training);

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(trainingAddRequest));

        verify(trainingTypeRepository, times(1)).findByName(trainingTypeName);
        verify(trainerRepository, times(1)).findByUserName(trainerUserName);
        verify(traineeRepository, times(0)).findByUserName(traineeUserName);
        verify(trainingRepository, times(0)).save(training);
    }

    @Test
    void createTrainingNotValidTraineeNameFail() {
        when(trainingTypeRepository.findByName(trainingTypeName)).thenReturn(Optional.empty());
        when(trainerRepository.findByUserName(trainerUserName)).thenReturn(Optional.ofNullable(trainer));
        when(traineeRepository.findByUserName(traineeUserName)).thenReturn(Optional.empty());
        when(trainingRepository.save(training)).thenReturn(training);

        assertThrows(EntityNotFoundException.class, () -> trainingService.create(trainingAddRequest));

        verify(trainingTypeRepository, times(1)).findByName(trainingTypeName);
        verify(trainerRepository, times(0)).findByUserName(trainerUserName);
        verify(traineeRepository, times(0)).findByUserName(traineeUserName);
        verify(trainingRepository, times(0)).save(training);
    }

    @Test
    void getTraineeTrainingsListCriteriaSuccess() {
        LocalDate fromDate = LocalDate.now().minusYears(10);
        LocalDate toDate = LocalDate.now().plusYears(10);

        List<Training> trainings = List.of(Training.builder()
                .trainee(Trainee.builder().user(User.builder().userName("Maria.Petrenko").build()).build())
                .trainer(Trainer.builder().user(User.builder().userName("Petro.Ivanenko").build()).build())
                .trainingType(TrainingType.builder().trainingTypeName("Zumba").build())
                .build());

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName("Maria.Petrenko")
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerUserName("Petro.Ivanenko")
                .trainingType("Zumba")
                .build();

        when(trainingRepository.getByTraineeCriteria(traineeTrainingsDto))
                .thenReturn(trainings);

        trainingService.getTraineeTrainingsListCriteria(traineeTrainingsDto);

        verify(trainingRepository, times(1))
                .getByTraineeCriteria(traineeTrainingsDto);

        verify(trainingMapper, times(1)).trainingToTraineeTrainingsListResponse(any());
        verify(traineeRepository, never()).findByUserName(any());
        verify(trainerRepository, never()).findByUserName(any());
        verify(trainingTypeRepository, never()).findByName(any());
    }

    @Test
    void getTrainerTrainingsListCriteriaSuccess() {
        LocalDate fromDate = LocalDate.now().minusYears(10);
        LocalDate toDate = LocalDate.now().plusYears(10);

        Trainer trainerForSearch = Trainer.builder().user(User.builder().userName("trainerUserName").build()).build();
        List<Training> trainings = List.of(Training.builder()
                .trainer(trainerForSearch)
                .trainee(Trainee.builder().user(User.builder().userName("traineeUserName").build()).build())
                .trainingType(TrainingType.builder().trainingTypeName("Zumba").build())
                .build());

        when(trainingRepository.getByTrainerCriteria(any())).thenReturn(trainings);
        when(trainerRepository.findByUserName("trainerUserName")).thenReturn(Optional.ofNullable(trainerForSearch));

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName("trainerUserName")
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName("Maria.Petrenko")
                .build();

        trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto);

        verify(trainingRepository, times(1))
                .getByTrainerCriteria(any());
    }
}
