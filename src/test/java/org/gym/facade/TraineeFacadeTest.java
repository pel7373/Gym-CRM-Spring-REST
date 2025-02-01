package org.gym.facade;

import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;
import org.gym.dto.UserDto;
import org.gym.facade.impl.TraineeFacadeImpl;
import org.gym.facade.impl.UserNameAndPasswordChecker;
import org.gym.service.TraineeService;
import org.gym.validator.UserDtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TraineeFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private UserDtoValidator userDtoValidator;

    @Mock
    private UserNameAndPasswordChecker userNameAndPasswordChecker;

    @InjectMocks
    private TraineeFacadeImpl traineeFacade;

    private TraineeDto traineeDto;
    private TraineeDto traineeDtoNotValid;
    private final String passwordForUser = "AAAAAAAAAA";
    private final String newPassword = "BBBBBBBBBB";
    String userNameForTraineeDto;
    String userNameNotFound = "bbbbbbb";

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        UserDto userDtoNotValid = new UserDto("Pa", "Pa", "Maria.Petrenko2", false);

        traineeDto = TraineeDto.builder()
                .user(userDto)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        userNameForTraineeDto = traineeDto.getUser().getFirstName();

        traineeDtoNotValid
                = new TraineeDto(userDtoNotValid, LocalDate.of(1995, 1, 23),
                "Kyiv, Soborna str. 35, ap. 26");
    }

    @Test
    void createTraineeSuccessfully() {
        when(userDtoValidator.validate(any(UserDto.class))).thenReturn(true);
        when(traineeService.create(traineeDto)).thenReturn(traineeDto);

        traineeFacade.create(traineeDto);

        verify(userDtoValidator, times(1)).validate(traineeDto.getUser());
        verify(traineeService, times(1)).create(any(TraineeDto.class));
    }

    @Test
    void createTraineeNullFail() {
        TraineeDto createdTraineeDto = traineeFacade.create(null);
        assertNull(createdTraineeDto);
        verify(userDtoValidator, never()).validate(traineeDto.getUser());
        verify(traineeService, never()).create(any(TraineeDto.class));
    }

    @Test
    void createTraineeNotValidFail() {
        when(userDtoValidator.validate(traineeDtoNotValid.getUser())).thenReturn(false);
        TraineeDto createdTraineeDto = traineeFacade.create(traineeDtoNotValid);

        assertNull(createdTraineeDto);
        verify(userDtoValidator, times(1)).validate(traineeDtoNotValid.getUser());
        verify(traineeService, never()).create(any(TraineeDto.class));
    }

    @Test
    void selectTraineeSuccessfully() {
        String gotUserNameForTraineeDto = traineeDto.getUser().getFirstName();
        when(userNameAndPasswordChecker.isNullOrBlank(any(String.class), any(String.class))).thenReturn(false);
        when(traineeService.authenticate(gotUserNameForTraineeDto, passwordForUser)).thenReturn(true);
        when(traineeService.select(gotUserNameForTraineeDto)).thenReturn(traineeDto);

        traineeFacade.select(gotUserNameForTraineeDto, passwordForUser);

        verify(traineeService, times(1)).authenticate(any(String.class), any(String.class));
        verify(traineeService, times(1)).authenticate(any(String.class), any(String.class));
        verify(traineeService, times(1)).select(gotUserNameForTraineeDto);
    }

    @Test
    void selectTraineeNotFound() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.select(userNameNotFound)).thenReturn(traineeDto);

        traineeFacade.select(userNameNotFound, passwordForUser);

        assertDoesNotThrow(() -> traineeService.select(userNameNotFound));
        verify(traineeService, times(1)).authenticate(any(String.class), any(String.class));
        verify(traineeService, times(1)).select(userNameNotFound);
    }

    @Test
    void updateTraineeSuccessfully() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTraineeDto, passwordForUser)).thenReturn(false);
        when(userDtoValidator.validate(traineeDto.getUser())).thenReturn(true);
        when(traineeService.authenticate(userNameForTraineeDto, passwordForUser)).thenReturn(true);
        when(traineeService.update(userNameForTraineeDto, traineeDto)).thenReturn(traineeDto);

        traineeFacade.update(userNameForTraineeDto, passwordForUser, traineeDto);

        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(any(String.class), any(String.class));
        verify(userDtoValidator, times(1)).validate(traineeDto.getUser());
        verify(traineeService, times(1)).authenticate(userNameForTraineeDto, passwordForUser);
        verify(traineeService, times(1)).update(userNameForTraineeDto, traineeDto);
    }

    @Test
    void updateTraineeNullFail() {
        TraineeDto createdTraineeDto = traineeFacade.update("aaa", "aaa", null);

        assertNull(createdTraineeDto);
        verify(userDtoValidator, never()).validate(traineeDto.getUser());
        verify(traineeService, never()).create(any(TraineeDto.class));
        verify(traineeService, never()).authenticate(any(String.class), any(String.class));
    }

    @Test
    void updateTraineeNotValidFail() {
        when(userDtoValidator.validate(traineeDtoNotValid.getUser())).thenReturn(false);
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTraineeDto, passwordForUser)).thenReturn(false);
        TraineeDto createdTraineeDto = traineeFacade.update("aaa", "aaa", traineeDtoNotValid);

        assertNull(createdTraineeDto);
        verify(userDtoValidator, times(1)).validate(traineeDtoNotValid.getUser());
        verify(userNameAndPasswordChecker, never()).isNullOrBlank(userNameForTraineeDto, passwordForUser);
        verify(traineeService, never()).update("aaa", traineeDtoNotValid);
    }

    @Test
    void updateTraineeNotFound() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(userDtoValidator.validate(traineeDtoNotValid.getUser())).thenReturn(true);
        when(traineeService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.update(userNameNotFound, traineeDtoNotValid)).thenReturn(traineeDto);

        traineeFacade.update(userNameNotFound, passwordForUser, traineeDtoNotValid);

        verify(userDtoValidator, times(1)).validate(traineeDtoNotValid.getUser());
        verify(traineeService, times(1)).authenticate(any(String.class), any(String.class));
        verify(traineeService, never()).update(userNameNotFound, traineeDtoNotValid);
    }


    @Test
    void deleteTraineeSuccessfully() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTraineeDto, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameForTraineeDto, passwordForUser)).thenReturn(true);

        traineeFacade.delete(userNameForTraineeDto, passwordForUser);

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(any(String.class), any(String.class));
        verify(traineeService, times(1)).authenticate(userNameForTraineeDto, passwordForUser);
        verify(traineeService, times(1)).delete(userNameForTraineeDto);
    }

    @Test
    void deleteTraineeNotValidFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);

        traineeFacade.delete(userNameNotFound, passwordForUser);

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(traineeService, never()).delete(userNameNotFound);
    }

    @Test
    void deleteTraineeNullFail() {
        traineeFacade.delete(null, passwordForUser);

        verify(traineeService, never()).delete(null);
        verify(traineeService, times(1)).authenticate(null, passwordForUser);
    }

    @Test
    void changeStatusStatusNullFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTraineeDto, passwordForUser)).thenReturn(false);
        TraineeDto createdTraineeDto = traineeFacade.changeStatus("aaa", "aaa", null);

        assertNull(createdTraineeDto);
        verify(userNameAndPasswordChecker, never()).isNullOrBlank(userNameForTraineeDto, passwordForUser);
        verify(traineeService, never()).changeStatus("aaa", null);
    }

    @Test
    void changeStatusUserNameNotFoundFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);

        TraineeDto createdTraineeDto = traineeFacade.changeStatus(
                userNameNotFound, passwordForUser, true);

        assertNull(createdTraineeDto);
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(traineeService, times(1))
                .authenticate(userNameNotFound, passwordForUser);
        verify(traineeService, never()).changeStatus(userNameNotFound, true);
    }

    @Test
    void changeStatusSuccessfully() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTraineeDto, passwordForUser))
                .thenReturn(false);
        when(traineeService.authenticate(userNameForTraineeDto, passwordForUser)).thenReturn(true);

        traineeFacade.changeStatus(userNameForTraineeDto, passwordForUser, true);

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameForTraineeDto, passwordForUser);
        verify(traineeService, times(1))
                .authenticate(userNameForTraineeDto, passwordForUser);
        verify(traineeService, times(1)).changeStatus(userNameForTraineeDto, true);
    }

    @Test
    void authenticateSuccessfully() {
        String gotUserNameForTraineeDto = traineeDto.getUser().getFirstName();
        when(userNameAndPasswordChecker.isNullOrBlank(any(String.class), any(String.class))).thenReturn(false);
        when(traineeService.authenticate(any(String.class), any(String.class))).thenReturn(true);

        traineeFacade.authenticate(gotUserNameForTraineeDto, passwordForUser);

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(any(String.class), any(String.class));
        verify(traineeService, times(1))
                .authenticate(any(String.class), any(String.class));
    }

    @Test
    void authenticateNotSuccessfulNullOrBlankUserNameOrPassword() {
        when(userNameAndPasswordChecker.isNullOrBlank(any(String.class), any(String.class))).thenReturn(true);

        traineeFacade.authenticate("aaa", "aaa");

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(any(String.class), any(String.class));
        verify(traineeService, never()).authenticate(any(String.class), any(String.class));
    }

    @Test
    void authenticateNotSuccessfulUserNameNotFoundOrDoesntMatchWithPassword() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);
        traineeFacade.authenticate(userNameNotFound, passwordForUser);

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(traineeService, times(1))
                .authenticate(any(String.class), any(String.class));
    }

    @Test
    void changePasswordNewPasswordNullFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(null)).thenReturn(true);
        TraineeDto createdTraineeDto = traineeFacade.changePassword(
                userNameForTraineeDto, passwordForUser, null);

        assertNull(createdTraineeDto);
        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(null);
        verify(traineeService, never()).changePassword(userNameForTraineeDto, null);
    }

    @Test
    void changePasswordUserNameNotFoundFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(newPassword)).thenReturn(false);
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);

        TraineeDto createdTraineeDto = traineeFacade.changePassword(
                userNameNotFound, passwordForUser, newPassword);

        assertNull(createdTraineeDto);
        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(newPassword);
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(traineeService, times(1))
                .authenticate(userNameNotFound, passwordForUser);
        verify(traineeService, never()).changePassword(userNameNotFound, newPassword);
    }

    @Test
    void changePasswordSuccessfully() {
        when(userNameAndPasswordChecker.isNullOrBlank(newPassword)).thenReturn(false);
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTraineeDto, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameForTraineeDto, passwordForUser)).thenReturn(true);

        traineeFacade.changePassword(userNameForTraineeDto, passwordForUser, newPassword);

        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(newPassword);
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameForTraineeDto, passwordForUser);
        verify(traineeService, times(1))
                .authenticate(userNameForTraineeDto, passwordForUser);
        verify(traineeService, times(1))
                .changePassword(userNameForTraineeDto, newPassword);
    }

    @Test
    void getUnassignedTrainersUserNameNullFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTraineeDto, passwordForUser)).thenReturn(false);
        List<TrainerDto> listTrainersDto = traineeFacade.getUnassignedTrainers("aaa", "aaa");

        assertNotNull(listTrainersDto);
        assertEquals(0, listTrainersDto.size());
        verify(userNameAndPasswordChecker, never()).isNullOrBlank(userNameForTraineeDto, passwordForUser);
        verify(traineeService, never()).getUnassignedTrainersList("aaa");
    }

    @Test
    void getUnassignedTrainersSuccessfully() {
        List<TrainerDto> listTrainersDto = List.of(new TrainerDto());
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameNotFound, passwordForUser)).thenReturn(true);
        when(traineeService.getUnassignedTrainersList(userNameNotFound))
                .thenReturn(listTrainersDto);

        List<TrainerDto> createdListTrainersDto = traineeFacade.getUnassignedTrainers(
                userNameNotFound, passwordForUser);

        assertNotNull(createdListTrainersDto);
        assertEquals(1, createdListTrainersDto.size());
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(traineeService, times(1))
                .authenticate(userNameNotFound, passwordForUser);
        verify(traineeService, times(1)).getUnassignedTrainersList(userNameNotFound);
    }

    @Test
    void getUnassignedTrainersUserNameNotFoundFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);

        List<TrainerDto> listTrainersDto = traineeFacade.getUnassignedTrainers(
                userNameNotFound, passwordForUser);

        assertNotNull(listTrainersDto);
        assertEquals(0, listTrainersDto.size());
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(traineeService, times(1))
                .authenticate(userNameNotFound, passwordForUser);
        verify(traineeService, never()).getUnassignedTrainersList(userNameNotFound);
    }

    @Test
    void updateTrainersListUserNameNullFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTraineeDto, passwordForUser))
                .thenReturn(false);
        List<TrainerDto> listTrainersDto =
                traineeFacade.updateTrainersList("aaa", "aaa", List.of("aa"));

        assertNotNull(listTrainersDto);
        assertEquals(0, listTrainersDto.size());
        verify(userNameAndPasswordChecker, never()).isNullOrBlank(userNameForTraineeDto, passwordForUser);
        verify(traineeService, never()).updateTrainersList("aaa", List.of("aa"));
    }

    @Test
    void updateTrainersListSuccessfully() {
        List<TrainerDto> listTrainersDto = List.of(new TrainerDto());
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTraineeDto, passwordForUser))
                .thenReturn(false);
        when(traineeService.authenticate(userNameForTraineeDto, passwordForUser))
                .thenReturn(true);
        when(traineeService.updateTrainersList(
            userNameForTraineeDto, List.of("aaa")))
            .thenReturn(listTrainersDto);

        List<TrainerDto> createdListTrainersDto = traineeFacade.updateTrainersList(
                userNameForTraineeDto, passwordForUser, List.of("aaa"));

        assertNotNull(createdListTrainersDto);
        assertEquals(1, createdListTrainersDto.size());
        assertEquals(listTrainersDto, createdListTrainersDto);
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameForTraineeDto, passwordForUser);
        verify(traineeService, times(1))
                .authenticate(userNameForTraineeDto, passwordForUser);
        verify(traineeService, times(1)).updateTrainersList(userNameForTraineeDto, List.of("aaa"));
    }

    @Test
    void updateTrainersListUserNameNotAuthenticatedFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(traineeService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);

        List<TrainerDto> listTrainersDto = traineeFacade.updateTrainersList(
                userNameNotFound, passwordForUser, List.of("aaa"));

        assertNotNull(listTrainersDto);
        assertEquals(0, listTrainersDto.size());
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(traineeService, times(1))
                .authenticate(userNameNotFound, passwordForUser);
        verify(traineeService, never()).updateTrainersList(userNameNotFound, List.of("aaa"));
    }

    @Test
    void updateTrainersListListOfTrainersNullFail() {
        List<TrainerDto> listTrainersDto = traineeFacade.updateTrainersList(
                userNameNotFound, passwordForUser, null);

        assertNotNull(listTrainersDto);
        assertEquals(0, listTrainersDto.size());
        verify(userNameAndPasswordChecker, never())
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(traineeService, never())
                .authenticate(userNameNotFound, passwordForUser);
        verify(traineeService, never()).updateTrainersList(userNameNotFound, null);
    }
}
