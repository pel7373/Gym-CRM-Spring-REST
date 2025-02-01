package org.gym.facade;

import org.gym.dto.*;
import org.gym.facade.impl.TrainingFacadeImpl;
import org.gym.facade.impl.UserNameAndPasswordChecker;
import org.gym.mapper.TrainingMapper;
import org.gym.service.TrainingService;
import org.gym.validator.TrainingDtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainingFacadeTest {

    @Mock
    private TrainingService trainingService;

    @Mock
    private UserNameAndPasswordChecker userNameAndPasswordChecker;

    @Mock
    private TrainingDtoValidator trainingDtoValidator;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingFacadeImpl trainingFacade;

    private TrainingDto trainingDto;

    @BeforeEach
    void setUp() {
        String trainingTypeName = "Zumba";
        TrainingTypeDto trainingTypeDto = TrainingTypeDto.builder()
                .trainingTypeName(trainingTypeName)
                .build();

        TraineeDto traineeDto = TraineeDto.builder()
                .user(new UserDto("Maria", "Petrenko", "Maria.Petrenko", true))
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        TrainerDto trainerDto = TrainerDto.builder()
                .user(new UserDto("Petro", "Ivanenko", "Petro.Ivanenko", true))
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        trainingDto = TrainingDto.builder()
                .trainingType(trainingTypeDto)
                .trainer(trainerDto)
                .trainee(traineeDto)
                .trainingName("Zumba next workout")
                .trainingType(trainingTypeDto)
                .date(LocalDate.now().plusDays(3))
                .duration(45)
                .build();
    }

    @Test
    void createTrainingSuccessfully() {
        when(trainingDtoValidator.validate(any())).thenReturn(true);
        when(trainingService.create(any())).thenReturn(trainingDto);

        TrainingDto createdTrainingDto = trainingFacade.create(trainingDto);
        assertAll(
                () -> assertNotNull(createdTrainingDto),
                () -> verify(trainingDtoValidator, times(1)).validate(any()),
                () -> verify(trainingService, times(1)).create(any()),
                () -> verify(trainingMapper, never()).convertToEntity(any()),
                () -> verify(trainingMapper, never()).convertToDto(any())
        );
    }

    @Test
    void createTrainingNullFail() {
        TrainingDto createdTrainingDto = trainingFacade.create(null);
        assertNull(createdTrainingDto);
        verify(trainingDtoValidator, never()).validate(any());
        verify(trainingService, never()).create(any());
    }

    @Test
    void createTraineeNotValidFail() {
        when(trainingDtoValidator.validate(any())).thenReturn(false);
        TrainingDto createdTrainingDto = trainingFacade.create(trainingDto);

        assertNull(createdTrainingDto);
        verify(trainingDtoValidator, times(1)).validate(any());
        verify(trainingService, never()).create(any());
    }

    @Test
    void getTraineeTrainingsListCriteriaSuccess() {
        LocalDate fromDate = LocalDate.now().minusYears(10);
        LocalDate toDate = LocalDate.now().plusYears(10);

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName("Name")
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerUserName("Name")
                .trainingType("stretching")
                .build();

        when(userNameAndPasswordChecker.isNullOrBlank(any())).thenReturn(false);

        trainingFacade.getTraineeTrainings(traineeTrainingsDto);

        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(any());
        verify(trainingService, times(1)).getTraineeTrainingsListCriteria(traineeTrainingsDto);
    }

    @Test
    void getTraineeTrainingsListFailNotValidTraineeUserName() {
        LocalDate fromDate = LocalDate.now().minusYears(10);
        LocalDate toDate = LocalDate.now().plusYears(10);

        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUserName(null)
                .fromDate(fromDate)
                .toDate(toDate)
                .trainerUserName("Name")
                .trainingType("stretching")
                .build();

        when(userNameAndPasswordChecker.isNullOrBlank(any())).thenReturn(true);

        trainingFacade.getTraineeTrainings(traineeTrainingsDto);

        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(any());
        verify(trainingService, never())
                .getTraineeTrainingsListCriteria(traineeTrainingsDto);
    }

    @Test
    void getTrainerTrainingsListSuccess() {
        LocalDate fromDate = LocalDate.now().minusYears(10);
        LocalDate toDate = LocalDate.now().plusYears(10);

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName("Name")
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName("Name")
                .build();

        when(userNameAndPasswordChecker.isNullOrBlank(any())).thenReturn(false);

        trainingFacade.getTrainerTrainings(trainerTrainingsDto);

        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(any());
        verify(trainingService, times(1))
                .getTrainerTrainingsListCriteria(any());
    }

    @Test
    void getTrainerTrainingsListFailNotValidTrainerUserName() {
        LocalDate fromDate = LocalDate.now().minusYears(10);
        LocalDate toDate = LocalDate.now().plusYears(10);

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUserName(null)
                .fromDate(fromDate)
                .toDate(toDate)
                .traineeUserName("Name")
                .build();

        when(userNameAndPasswordChecker.isNullOrBlank(any())).thenReturn(true);

        trainingFacade.getTrainerTrainings(trainerTrainingsDto);

        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(any());
        verify(trainingService, never()).getTrainerTrainingsListCriteria(any());
    }
}
