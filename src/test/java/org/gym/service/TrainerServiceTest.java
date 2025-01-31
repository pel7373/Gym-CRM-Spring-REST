package org.gym.service;

import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.entity.*;
import org.gym.entity.Trainer;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainerMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.impl.TrainerServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerServiceTest {
    
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private UserNameGeneratorService userNameGeneratorService;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private TrainerDto trainerDto;
    private Trainer trainer;
    private final String passwordForUser = "AAAAAAAAAA";
    private String userNameForTrainerDto;
    private TrainingType trainerTrainingType;
    private String userNameForTrainer;
    private TrainingTypeDto trainerTrainingTypeDto;
    private final TrainingType trainingType = TrainingType.builder()
            .trainingTypeName("Zumba")
            .build();

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        User user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", passwordForUser, true);

        trainerDto = TrainerDto.builder()
                .user(userDto)
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        trainer = Trainer.builder()
                .user(user)
                .specialization(TrainingType.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        trainerTrainingType = trainer.getSpecialization();
        trainerTrainingTypeDto = trainerDto.getSpecialization();

        userNameForTrainerDto = trainerDto.getUser().getUserName();
        userNameForTrainer = trainer.getUser().getUserName();
    }

    @Test
    void createTrainerSuccessfully() {
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        when(trainingTypeRepository.findByName(trainerTrainingTypeDto.getTrainingTypeName()))
                .thenReturn(Optional.ofNullable(trainerTrainingType));
        when(trainerMapper.convertToEntity(trainerDto)).thenReturn(trainer);
        when(trainerMapper.convertToDto(trainer)).thenReturn(trainerDto);

        trainerService.create(trainerDto);
        verify(trainerRepository, times(1)).save(any(Trainer.class));
        verify(trainingTypeRepository, times(1)).findByName(any(String.class));
        verify(trainerMapper, times(1)).convertToEntity(trainerDto);
        verify(trainerMapper, times(1)).convertToDto(trainer);
    }

    @Test
    void selectTrainerSuccessfully() {
        userNameForTrainerDto = trainerDto.getUser().getFirstName();
        when(trainerRepository.findByUserName(userNameForTrainerDto)).thenReturn(Optional.ofNullable(trainer));

        trainerService.select(userNameForTrainerDto);

        verify(trainerRepository, times(1)).findByUserName(userNameForTrainerDto);
    }

    @Test
    void updateExistingTrainerSuccessfully() {
        UserDto userDto = new UserDto("John", "Doe", "John.Doe", true);
        trainerDto = TrainerDto.builder()
                .user(userDto)
                .specialization(trainerTrainingTypeDto)
                .build();

        User userForUpdate = new User(2L, "Maria", "Ivanova", "Maria.Ivanova", "BBBBBBBBBB", true);
        Trainer trainerForUpdate = Trainer.builder()
                .id(2L)
                .user(userForUpdate)
                .specialization(trainerTrainingType)
                .build();

        User userUpdated = new User(2L, "John", "Doe", "John.Doe", "BBBBBBBBBB", true);
        Trainer trainerUpdated = Trainer.builder()
                .id(2L)
                .user(userUpdated)
                .specialization(trainerTrainingType)
                .build();

        UserDto userDtoUpdated = new UserDto("John", "Doe", "John.Doe", true);
        TrainerDto trainerDtoUpdated = TrainerDto.builder()
                .user(userDtoUpdated)
                .specialization(trainerTrainingTypeDto)
                .build();


        when(trainerRepository.findByUserName(userForUpdate.getUserName()))
                .thenReturn(Optional.ofNullable(trainerForUpdate));
        when(userNameGeneratorService.generate(userDto.getFirstName(), userDto.getLastName()))
                .thenReturn(userDto.getUserName());
        when(trainerRepository.save(trainerUpdated)).thenReturn(trainerUpdated);
        when(trainerMapper.convertToDto(trainerUpdated)).thenReturn(trainerDtoUpdated);
        when(trainingTypeRepository.findByName(trainerTrainingTypeDto.getTrainingTypeName()))
                .thenReturn(Optional.ofNullable(trainerTrainingType));
        when(trainerMapper.convertToEntity(trainerDto)).thenReturn(trainer);

        TrainerDto trainerDtoActual = trainerService.update(userForUpdate.getUserName(), trainerDto);

        assertNotNull(trainerDtoActual);
        assertAll(
                "Grouped assertions of selected trainerDto",
                () -> assertEquals(trainerDtoUpdated.getUser().getFirstName(),
                        trainerDtoActual.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals(trainerDtoUpdated.getUser().getLastName(),
                        trainerDtoActual.getUser().getLastName(), "lastName should be Petrenko"),
                () -> assertEquals(trainerDtoUpdated.getSpecialization(),
                        trainerDtoActual.getSpecialization(), "specialization should be equal")
        );

        verify(trainerRepository, times(1)).findByUserName("Maria.Ivanova");
        verify(trainerRepository, times(1)).save(trainerUpdated);
        verify(userNameGeneratorService, times(1))
                .generate(userDto.getFirstName(), userDto.getLastName());
        verify(trainerMapper, times(1)).convertToDto(trainerUpdated);
        verify(trainingTypeRepository, times(1))
                .findByName(trainerTrainingTypeDto.getTrainingTypeName());
        verify(trainerMapper, never()).convertToEntity(trainerDto);
    }
    
    @Test
    void changeStatusSuccessfullyWhenStatusDifferent() {
        when(trainerRepository.findByUserName(userNameForTrainerDto)).thenReturn(Optional.ofNullable(trainer));
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        when(trainerMapper.convertToDto(trainer)).thenReturn(trainerDto);

        trainerService.changeStatus(userNameForTrainerDto, !trainer.getUser().getIsActive());

        verify(trainerRepository, times(1)).findByUserName(userNameForTrainerDto);
        verify(trainerRepository, times(1)).save(trainer);
        verify(trainerMapper, times(1)).convertToDto(trainer);
    }

    @Test
    void changeStatusSuccessfullyWhenStatusTheSame() {
        when(trainerRepository.findByUserName(userNameForTrainerDto)).thenReturn(Optional.ofNullable(trainer));
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        when(trainerMapper.convertToDto(trainer)).thenReturn(trainerDto);

        trainerService.changeStatus(userNameForTrainerDto, trainer.getUser().getIsActive());

        verify(trainerRepository, times(1)).findByUserName(userNameForTrainerDto);
        verify(trainerRepository, times(1)).save(trainer);
        verify(trainerMapper, times(1)).convertToDto(trainer);
    }

    @Test
    void changePasswordSuccessfully() {
        when(trainerRepository.findByUserName(userNameForTrainerDto)).thenReturn(Optional.ofNullable(trainer));

        String newPassword = "BBBBBBBBBB";
        trainerService.changePassword(userNameForTrainerDto, newPassword);

        verify(trainerRepository, times(1)).findByUserName(userNameForTrainerDto);
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void changeSpecializationSuccessfully() {
        when(trainerRepository.findByUserName(userNameForTrainerDto)).thenReturn(Optional.ofNullable(trainer));

        trainerService.changeSpecialization(userNameForTrainerDto, trainingType);

        verify(trainerRepository, times(1)).findByUserName(userNameForTrainerDto);
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void authenticateSuccessfully() {
        when(trainerRepository.findByUserName(userNameForTrainer)).thenReturn(Optional.ofNullable(trainer));

        boolean isAuthenticate = trainerService.authenticate(userNameForTrainer, passwordForUser);

        assertTrue(isAuthenticate);
        verify(trainerRepository, times(1)).findByUserName(any(String.class));
    }

    @Test
    void authenticateNotValidPasswordNotSuccessful() {
        when(trainerRepository.findByUserName(userNameForTrainer)).thenReturn(Optional.ofNullable(trainer));

        boolean isAuthenticate = trainerService.authenticate(userNameForTrainer, "NotValidPassword");

        assertFalse(isAuthenticate);
        verify(trainerRepository, times(1)).findByUserName(any(String.class));
    }

    @Test
    void getUnassignedTrainersList() {
        String traineeUserName = "Ivan.Ivanenko";

        Trainee trainee1 = Trainee.builder()
                .user(User.builder()
                        .userName(traineeUserName)
                        .firstName("Ivan")
                        .lastName("Ivanenko")
                        .password("password")
                        .build())
                .address("Vinnitsya, Soborna str.")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        Trainer trainer1 = Trainer.builder()
                .trainees(List.of(trainee1))
                .user(User.builder()
                        .firstName("Petro")
                        .lastName("Petrenko")
                        .userName("Petro.Petrenko")
                        .password("password")
                        .build())
                .specialization(TrainingType.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        TrainerDto trainerDto1 = TrainerDto.builder()
                .user(UserDto.builder()
                        .userName("Petro")
                        .firstName("Petrenko")
                        .lastName("Petro.Petrenko")
                        .build())
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        Trainer trainer2 = Trainer.builder()
                .trainees(List.of())
                .user(User.builder()
                        .firstName("PetroPetro")
                        .lastName("Petrenko")
                        .userName("PetroPetro.Petrenko")
                        .password("password")
                        .build())
                .specialization(TrainingType.builder()
                        .trainingTypeName("yoga")
                        .build())
                .build();


        TrainerDto trainerDto2 = TrainerDto.builder()
                .user(UserDto.builder()
                        .userName("PetroPetro.Petrenko")
                        .firstName("PetroPetro")
                        .lastName("Petrenko")
                        .build())
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("yoga")
                        .build())
                .build();

        when(traineeRepository.findByUserName(traineeUserName)).thenReturn(Optional.of(trainee1));
        when(trainerMapper.convertToDto(trainer1)).thenReturn(trainerDto1);
        when(trainerMapper.convertToDto(trainer2)).thenReturn(trainerDto2);
        when(trainerRepository.findAll()).thenReturn(List.of(trainer1, trainer2));

        List<TrainerDto> unassignedTrainers = trainerService.getUnassignedTrainersList(traineeUserName);

        assertNotNull(unassignedTrainers);
        assertEquals(1, unassignedTrainers.size());

        assertEquals("PetroPetro.Petrenko", unassignedTrainers.get(0).getUser().getUserName());

        verify(traineeRepository, times(1)).findByUserName(traineeUserName);
        verify(trainerRepository, times(1)).findAll();
    }


    @Test
    void getUnassignedTrainersListNotFound() {
        String traineeUserName = "NameNotFound";

        when(trainingTypeRepository.findByName(traineeUserName)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainerService.getUnassignedTrainersList(traineeUserName));
    }

    @Test
    void isFirstOrLastNamesChangedDoesntChangeNames() {
        UserDto userDto2 = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        TrainerDto trainerDto2 = TrainerDto.builder()
                .user(userDto2)
                .build();

        boolean result = trainerService.isFirstOrLastNamesChanged(trainerDto2, trainer);
        assertAll(
                () -> assertFalse(result),
                () -> assertEquals(trainer.getUser().getFirstName(), trainerDto2.getUser().getFirstName()),
                () -> assertEquals(trainer.getUser().getLastName(), trainerDto2.getUser().getLastName())
        );
    }

    @Test
    void isFirstOrLastNamesChangedFirstNames() {
        UserDto userDto2 = new UserDto("Iryna", "Petrenko", "Maria.Petrenko", true);
        TrainerDto trainerDto2 = TrainerDto.builder()
                .user(userDto2)
                .build();

        boolean result = trainerService.isFirstOrLastNamesChanged(trainerDto2, trainer);
        assertAll(
                () -> assertTrue(result),
                () -> assertNotEquals(trainer.getUser().getFirstName(), trainerDto2.getUser().getFirstName()),
                () -> assertEquals(trainer.getUser().getLastName(), trainerDto2.getUser().getLastName())
        );
    }

    @Test
    void isFirstOrLastNamesChangedBothNames() {
        UserDto userDto2 = new UserDto("Iryna", "Sergienko", "Maria.Petrenko", true);
        TrainerDto trainerDto2 = TrainerDto.builder()
                .user(userDto2)
                .build();

        boolean result = trainerService.isFirstOrLastNamesChanged(trainerDto2, trainer);
        assertAll(
                () -> assertTrue(result),
                () -> assertNotEquals(trainer.getUser().getFirstName(), trainerDto2.getUser().getFirstName()),
                () -> assertNotEquals(trainer.getUser().getLastName(), trainerDto2.getUser().getLastName())
        );
    }
}
