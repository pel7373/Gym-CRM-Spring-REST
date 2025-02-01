package org.gym.service;

import jakarta.transaction.Transactional;
import org.gym.DataStorage;
import org.gym.dto.TrainerDto;
import org.gym.dto.UserDto;
import org.gym.entity.Trainee;
import org.gym.dto.TraineeDto;
import org.gym.entity.User;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TraineeMapper;
import org.gym.mapper.TrainerMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserNameGeneratorService userNameGeneratorService;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private final DataStorage ds = new DataStorage();
    private Trainee trainee;
    private TraineeDto traineeDto;
    private TraineeDto traineeDtoUpdated;
    private String userNameForTrainee;

    private final String passwordForUser = "AAAAAAAAAA";
    private final String userNameNotFound = "bbbbbbb";

    UserDto userDto;
    User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", passwordForUser, true);

        traineeDto = TraineeDto.builder()
                .user(userDto)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        userNameForTrainee = trainee.getUser().getUserName();
    }

    @Test
    void selectIfExist() {
        when(traineeRepository.findByUserName(userNameForTrainee)).thenReturn(Optional.ofNullable(trainee));
        when(traineeMapper.convertToDto(trainee)).thenReturn(traineeDto);

        traineeService.select(userNameForTrainee);

        verify(traineeRepository, times(1)).findByUserName(userNameForTrainee);
        verify(traineeMapper, times(1)).convertToDto(trainee);
    }

    @Test
    void selectNotFound() {
        String exceptionMessage = String.format(ENTITY_NOT_FOUND_EXCEPTION, userNameNotFound);
        when(traineeRepository.findByUserName(userNameNotFound))
                .thenThrow(new EntityNotFoundException(exceptionMessage));
        assertThrows(EntityNotFoundException.class, () -> traineeService.select(userNameNotFound), exceptionMessage);
        verify(traineeRepository, times(1)).findByUserName(userNameNotFound);
    }

    @Test
    void selectNullThenException() {
        String exceptionMessage = String.format(ENTITY_NOT_FOUND_EXCEPTION, (Object) null);
        assertThrows(EntityNotFoundException.class, () -> traineeService.select(null), exceptionMessage);
        verify(traineeRepository, times(1)).findByUserName(null);
    }

    @Test
    void createTraineeSuccessfully() {
        when(userNameGeneratorService.generate(trainee.getUser().getFirstName(), trainee.getUser().getLastName())).thenReturn("John.Doe");
        when(passwordGeneratorService.generate()).thenReturn("AAAAAAAAAA");
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(traineeMapper.convertToEntity(traineeDto)).thenReturn(trainee);
        when(traineeMapper.convertToDto(trainee)).thenReturn(traineeDto);

        TraineeDto traineeDtoActual = traineeService.create(traineeDto);

        assertNotNull(traineeDtoActual);
        assertEquals(traineeDto, traineeDtoActual);
        verify(userNameGeneratorService, times(1)).generate(any(String.class), any(String.class));
        verify(passwordGeneratorService, times(1)).generate();
        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void updateExistingTraineeSuccessfully() {
        userDto = new UserDto("John", "Doe", "John.Doe", true);
        traineeDto = TraineeDto.builder()
                .user(userDto)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        User userForUpdate = new User(2L, "Maria", "Ivanova", "Maria.Ivanova", "BBBBBBBBBB", true);
        Trainee traineeForUpdate = Trainee.builder()
                .id(2L)
                .user(userForUpdate)
                .dateOfBirth(LocalDate.of(2000, 2, 13))
                .address("Kyiv, Soborna str. 35, ap. 26")
                .build();

        User userUpdated = new User(2L, "John", "Doe", "John.Doe", "BBBBBBBBBB", true);
        Trainee traineeUpdated = Trainee.builder()
                .id(2L)
                .user(userUpdated)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        UserDto userDtoUpdated = new UserDto("John", "Doe", "John.Doe", true);
        traineeDtoUpdated = TraineeDto.builder()
                .user(userDtoUpdated)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();


        when(traineeRepository.findByUserName(userForUpdate.getUserName())).thenReturn(Optional.ofNullable(traineeForUpdate));
        when(userNameGeneratorService.generate(userDto.getFirstName(), userDto.getLastName())).thenReturn(userDto.getUserName());
        //it's the key point - the service has prepared the correct entity for the update:
        when(traineeRepository.save(traineeUpdated)).thenReturn(traineeUpdated);
        when(traineeMapper.convertToDto(traineeUpdated)).thenReturn(traineeDtoUpdated);

        TraineeDto traineeDtoActual = traineeService.update(userForUpdate.getUserName(), traineeDto);

        assertNotNull(traineeDtoActual);
        assertAll(
                "Grouped assertions of selected traineeDto",
                () -> assertEquals(traineeDtoUpdated.getUser().getFirstName(),
                        traineeDtoActual.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals(traineeDtoUpdated.getUser().getLastName(),
                        traineeDtoActual.getUser().getLastName(), "lastName should be Petrenko"),
                () -> assertEquals(traineeDtoUpdated.getAddress(), traineeDtoActual.getAddress(),
                        "addresses should be equal"),
                () -> assertEquals(traineeDtoUpdated.getDateOfBirth(), traineeDtoActual.getDateOfBirth(),
                        "dates of birth should be equal")
        );

        verify(traineeRepository, times(1)).findByUserName("Maria.Ivanova");
        verify(traineeRepository, times(1)).save(traineeUpdated);
    }

    @Test
    void updateNullThenException() {
        String exceptionMessage = String.format(ENTITY_NOT_FOUND_EXCEPTION, (Object) null);
        assertThrows(EntityNotFoundException.class, () -> traineeService.update(null, traineeDto), exceptionMessage);
        verify(traineeRepository, times(1)).findByUserName(null);
    }

    @Test
    void deleteTraineeSuccessfully() {
        when(traineeRepository.findByUserName(userNameForTrainee)).thenReturn(Optional.ofNullable(trainee));

        traineeService.delete(userNameForTrainee);

        verify(traineeRepository, times(1)).delete(userNameForTrainee);
    }

    @Test
    void changeStatusSuccessfullyWhenStatusDifferent() {
        when(traineeRepository.findByUserName(userNameForTrainee)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(traineeMapper.convertToDto(trainee)).thenReturn(traineeDto);

        traineeService.changeStatus(userNameForTrainee, !trainee.getUser().getIsActive());

        verify(traineeRepository, times(1)).findByUserName(userNameForTrainee);
        verify(traineeRepository, times(1)).save(trainee);
        verify(traineeMapper, times(1)).convertToDto(trainee);
    }

    @Test
    void changeStatusSuccessfullyWhenStatusTheSame() {
        when(traineeRepository.findByUserName(userNameForTrainee)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(traineeMapper.convertToDto(trainee)).thenReturn(traineeDto);

        traineeService.changeStatus(userNameForTrainee, trainee.getUser().getIsActive());

        verify(traineeRepository, times(1)).findByUserName(userNameForTrainee);
        verify(traineeRepository, times(1)).save(trainee);
        verify(traineeMapper, times(1)).convertToDto(trainee);
    }

    @Test
    void changeStatusNullThenException() {
        String exceptionMessage = String.format(ENTITY_NOT_FOUND_EXCEPTION, (Object) null);
        assertThrows(EntityNotFoundException.class, () -> traineeService.changeStatus(null, true), exceptionMessage);
        verify(traineeRepository, times(1)).findByUserName(null);
    }

    @Test
    void changePasswordSuccessfully() {
        when(traineeRepository.findByUserName(userNameForTrainee)).thenReturn(Optional.ofNullable(trainee));

        String newPassword = "BBBBBBBBBB";
        traineeService.changePassword(userNameForTrainee, newPassword);

        verify(traineeRepository, times(1)).findByUserName(userNameForTrainee);
        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void changePasswordNullThenException() {
        String exceptionMessage = String.format(ENTITY_NOT_FOUND_EXCEPTION, (Object) null);
        assertThrows(EntityNotFoundException.class, () -> traineeService.changePassword(null, "bbbb"), exceptionMessage);
        verify(traineeRepository, times(1)).findByUserName(null);
    }


    @Test
    void authenticateSuccessfully() {
        when(traineeRepository.findByUserName(userNameForTrainee)).thenReturn(Optional.ofNullable(trainee));

        boolean isAuthenticate = traineeService.authenticate(userNameForTrainee, passwordForUser);

        assertTrue(isAuthenticate);
        verify(traineeRepository, times(1)).findByUserName(any(String.class));
    }

    @Test
    void authenticateNotValidPasswordNotSuccessful() {
        when(traineeRepository.findByUserName(userNameForTrainee)).thenReturn(Optional.ofNullable(trainee));

        boolean isAuthenticate = traineeService.authenticate(userNameForTrainee, "NotValidPassword");

        assertFalse(isAuthenticate);
        verify(traineeRepository, times(1)).findByUserName(any(String.class));
    }

    @Test
    void authenticateStatusNullThenException() {
        boolean result = traineeService.authenticate(null, passwordForUser);
        assertFalse(result);
        verify(traineeRepository, times(1)).findByUserName(null);
    }

    @Test
    void getUnassignedTrainersListSuccessfully() {
        when(traineeRepository.findByUserName(ds.traineeUserName)).thenReturn(Optional.of(ds.trainee1));
        when(trainerMapper.convertToDto(ds.trainer1)).thenReturn(ds.trainerDto1);
        when(trainerMapper.convertToDto(ds.trainer2)).thenReturn(ds.trainerDto2);
        when(trainerRepository.findAll()).thenReturn(List.of(ds.trainer1, ds.trainer2));

        List<TrainerDto> unassignedTrainers = traineeService.getUnassignedTrainersList(ds.traineeUserName);

        assertNotNull(unassignedTrainers);
        assertEquals(1, unassignedTrainers.size());

        assertEquals("PetroPetro.Petrenko", unassignedTrainers.get(0).getUser().getUserName());

        verify(traineeRepository, times(1)).findByUserName(ds.traineeUserName);
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    void getUnassignedTrainersListNotFound() {
        String traineeUserName = "NameNotFound";

        when(traineeRepository.findByUserName(traineeUserName)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> traineeService.getUnassignedTrainersList(traineeUserName));
        verify(traineeRepository, times(1)).findByUserName(traineeUserName);
    }

    @Test
    void updateTrainersListSuccessfully() {
        when(traineeRepository.findByUserName(ds.traineeUserName))
                .thenReturn(Optional.of(ds.trainee1));

        when(trainerRepository.findByUserName(ds.trainer1.getUser().getUserName()))
                .thenReturn(Optional.of(ds.trainer1));
        when(trainerRepository.findByUserName(ds.trainer2.getUser().getUserName()))
                .thenReturn(Optional.of(ds.trainer2));
        when(trainerMapper.convertToDto(ds.trainer1)).thenReturn(ds.trainerDto1);
        when(trainerMapper.convertToDto(ds.trainer2)).thenReturn(ds.trainerDto2);
        List<String> listTrainersUserNames = List.of(
                ds.trainer1.getUser().getUserName(),
                ds.trainer2.getUser().getUserName());

        List<TrainerDto> updatedTrainersListList =
                traineeService.updateTrainersList(ds.traineeUserName, listTrainersUserNames);

        assertAll(
                () -> assertNotNull(updatedTrainersListList),
                () -> assertEquals(2, updatedTrainersListList.size())
        );

        verify(traineeRepository, times(1)).findByUserName(ds.traineeUserName);
        verify(trainerRepository, times(2)).findByUserName(any());
        verify(trainerMapper, times(2)).convertToDto(any());
    }

    @Test
    void updateTrainersListTraineeNotFound() {
        String traineeUserName = "NameNotFound";

        when(traineeRepository.findByUserName(traineeUserName)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> traineeService.updateTrainersList(traineeUserName, List.of()));
        verify(traineeRepository, times(1)).findByUserName(traineeUserName);
        verify(trainerRepository, never()).findByUserName(any());
    }

    @Test
    void isFirstOrLastNamesChangedDoesntChangeNames() {
        UserDto userDto2 = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        TraineeDto traineeDto2 = TraineeDto.builder()
                .user(userDto2)
                .build();

        boolean result = traineeService.isFirstOrLastNamesChanged(traineeDto2, trainee);
        assertAll(
                () -> assertFalse(result),
                () -> assertEquals(trainee.getUser().getFirstName(), traineeDto2.getUser().getFirstName()),
                () -> assertEquals(trainee.getUser().getLastName(), traineeDto2.getUser().getLastName())
        );
    }

    @Test
    void isFirstOrLastNamesChangedFirstNames() {
        UserDto userDto2 = new UserDto("Iryna", "Petrenko", "Maria.Petrenko", true);
        TraineeDto traineeDto2 = TraineeDto.builder()
                .user(userDto2)
                .build();

        boolean result = traineeService.isFirstOrLastNamesChanged(traineeDto2, trainee);
        assertAll(
                () -> assertTrue(result),
                () -> assertNotEquals(trainee.getUser().getFirstName(), traineeDto2.getUser().getFirstName()),
                () -> assertEquals(trainee.getUser().getLastName(), traineeDto2.getUser().getLastName())
        );
    }

    @Test
    void isFirstOrLastNamesChangedBothNames() {
        UserDto userDto2 = new UserDto("Iryna", "Sergienko", "Maria.Petrenko", true);
        TraineeDto traineeDto2 = TraineeDto.builder()
                .user(userDto2)
                .build();

        boolean result = traineeService.isFirstOrLastNamesChanged(traineeDto2, trainee);
        assertAll(
                () -> assertTrue(result),
                () -> assertNotEquals(trainee.getUser().getFirstName(), traineeDto2.getUser().getFirstName()),
                () -> assertNotEquals(trainee.getUser().getLastName(), traineeDto2.getUser().getLastName())
        );
    }
}
