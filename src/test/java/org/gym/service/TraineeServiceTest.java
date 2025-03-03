package org.gym.service;

import jakarta.transaction.Transactional;
import org.gym.DataStorage;
import org.gym.dto.TraineeDto;
import org.gym.dto.UserDto;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.request.user.UserUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.dto.response.user.UserUpdateResponse;
import org.gym.entity.*;
import org.gym.entity.Trainee;
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

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private final DataStorage ds = new DataStorage();
    private Trainee trainee;
    private TraineeDto traineeDto;
    private String userNameForTrainee;

    private final String userNameNotFound = "bbbbbbb";

    UserDto userDto;
    User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        String passwordForUser = "AAAAAAAAAA";
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
        when(traineeMapper.convertTraineeToTraineeSelectResponse(trainee)).thenReturn(any(TraineeSelectResponse.class));

        traineeService.select(userNameForTrainee);

        verify(traineeRepository, times(1)).findByUserName(userNameForTrainee);
        verify(traineeMapper, times(1)).convertTraineeToTraineeSelectResponse(trainee);
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
        when(userNameGeneratorService.generate(ds.traineeDto.getUser().getFirstName(), ds.traineeDto.getUser().getLastName()))
                .thenReturn(ds.traineeDto.getUser().getUserName());
        when(passwordGeneratorService.generate()).thenReturn("AAAAAAAAAA");
        when(traineeRepository.save(ds.trainee1)).thenReturn(ds.trainee1);
        when(traineeMapper.convertToEntity(traineeDto)).thenReturn(ds.trainee1);
        when(traineeMapper.convertToCreateResponse(ds.trainee1)).thenReturn(ds.traineeCreateResponse);

        CreateResponse createResponse = traineeService.create(traineeDto);

        assertNotNull(createResponse);
        assertEquals(ds.traineeDto.getUser().getUserName(), createResponse.getUserName());
        verify(userNameGeneratorService, times(1)).generate(any(String.class), any(String.class));
        verify(passwordGeneratorService, times(1)).generate();
        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void updateExistingTraineeSuccessfully() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("John", "Doe", true);
        TraineeUpdateRequest traineeUpdateRequest = TraineeUpdateRequest.builder()
                .user(userUpdateRequest)
                .build();

        User userForUpdate = new User(2L, "Maria", "Ivanova", "Maria.Ivanova", "BBBBBBBBBB", true);
        Trainee traineeForUpdate = Trainee.builder()
                .id(2L)
                .user(userForUpdate)
                .build();

        User userUpdated = new User(2L, "John", "Doe", "Maria.Ivanova", "BBBBBBBBBB", true);
        Trainee traineeUpdated = Trainee.builder()
                .id(2L)
                .user(userUpdated)
                .build();

        UserUpdateResponse userUpdateResponse = new UserUpdateResponse("John", "Doe", "Maria.Ivanova", true);
        TraineeUpdateResponse traineeUpdateResponse = TraineeUpdateResponse.builder()
                .user(userUpdateResponse)
                .build();

        when(traineeRepository.findByUserName(userForUpdate.getUserName()))
                .thenReturn(Optional.ofNullable(traineeForUpdate));
        when(traineeRepository.save(traineeUpdated)).thenReturn(traineeUpdated);
        when(traineeMapper.convertTraineeToTraineeUpdateResponse(traineeUpdated)).thenReturn(traineeUpdateResponse);

        TraineeUpdateResponse traineeUpdateResponseActual = traineeService.update(userForUpdate.getUserName(), traineeUpdateRequest);

        assertAll(
                "Grouped assertions of selected traineeDto",
                () -> assertNotNull(traineeUpdateResponseActual),
                () -> assertEquals(traineeUpdateResponse.getUser().getFirstName(),
                        traineeUpdateResponseActual.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals(traineeUpdateResponse.getUser().getLastName(),
                        traineeUpdateResponseActual.getUser().getLastName(), "lastName should be Petrenko")
        );

        verify(traineeRepository, times(1)).findByUserName("Maria.Ivanova");
        verify(traineeRepository, times(1)).save(traineeUpdated);
        verify(userNameGeneratorService, never())
                .generate(any(String.class), any(String.class));
        verify(traineeMapper, times(1))
                .convertTraineeToTraineeUpdateResponse(any(Trainee.class));
    }

    @Test
    void deleteTraineeSuccessfully() {
        when(traineeRepository.findByUserName(userNameForTrainee)).thenReturn(Optional.ofNullable(trainee));

        traineeService.delete(userNameForTrainee);

        verify(traineeRepository, times(1)).delete(userNameForTrainee);
    }

    @Test
    void getUnassignedTrainersListSuccessfully() {
        when(traineeRepository.findByUserName(ds.trainee1.getUser().getUserName()))
                .thenReturn(Optional.of(ds.trainee1));
        when(trainerRepository.findAll()).thenReturn(List.of(ds.trainer1, ds.trainer2));

        traineeService.getUnassignedTrainersList(ds.trainee1.getUser().getUserName());

        verify(traineeRepository, times(1)).findByUserName(ds.trainee1.getUser().getUserName());
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
        List<String> listTrainersUserNames = List.of(
                ds.trainer1.getUser().getUserName(),
                ds.trainer2.getUser().getUserName());

        traineeService.updateTrainersList(ds.traineeUserName, listTrainersUserNames);

        verify(traineeRepository, times(1)).findByUserName(ds.traineeUserName);
        verify(trainerRepository, times(2)).findByUserName(any());
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
}
