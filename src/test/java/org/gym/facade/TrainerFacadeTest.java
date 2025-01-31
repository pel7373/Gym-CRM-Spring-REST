package org.gym.facade;

import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.entity.TrainingType;
import org.gym.facade.impl.TrainerFacadeImpl;
import org.gym.facade.impl.UserNameAndPasswordChecker;
import org.gym.service.TrainerService;
import org.gym.validator.UserDtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerFacadeTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserDtoValidator userDtoValidator;

    @Mock
    private UserNameAndPasswordChecker userNameAndPasswordChecker;

    @InjectMocks
    private TrainerFacadeImpl trainerFacade;

    private TrainerDto trainerDto;
    private TrainerDto trainerDtoNotValid;
    private final String passwordForUser = "AAAAAAAAAA";
    private final String newPassword = "BBBBBBBBBB";
    private String userNameForTrainerDto;
    private final String userNameNotFound = "bbbbbbb";

    private final TrainingType trainingType = TrainingType.builder()
            .trainingTypeName("Zumba")
            .build();



    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        UserDto userDtoNotValid = new UserDto("Pa", "Pa", "Maria.Petrenko2", false);

        trainerDto = TrainerDto.builder()
                .user(userDto)
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        userNameForTrainerDto = trainerDto.getUser().getFirstName();

        trainerDtoNotValid = TrainerDto.builder()
                .user(userDtoNotValid)
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();
    }

    @Test
    void createTrainerSuccessfully() {
        when(userDtoValidator.validate(any(UserDto.class))).thenReturn(true);
        when(trainerService.create(trainerDto)).thenReturn(trainerDto);

        trainerFacade.create(trainerDto);

        verify(userDtoValidator, times(1)).validate(trainerDto.getUser());
        verify(trainerService, times(1)).create(any(TrainerDto.class));
    }

    @Test
    void createTrainerNullFail() {
        TrainerDto createdTrainerDto = trainerFacade.create(null);
        assertNull(createdTrainerDto);
        verify(userDtoValidator, never()).validate(trainerDto.getUser());
        verify(trainerService, never()).create(any(TrainerDto.class));
    }

    @Test
    void createTrainerNotValidFail() {
        when(userDtoValidator.validate(trainerDtoNotValid.getUser())).thenReturn(false);
        TrainerDto createdTrainerDto = trainerFacade.create(trainerDtoNotValid);

        assertNull(createdTrainerDto);
        verify(userDtoValidator, times(1)).validate(trainerDtoNotValid.getUser());
        verify(trainerService, never()).create(any(TrainerDto.class));
    }

    @Test
    void selectTrainerSuccessfully() {
        String selectedUserNameForTrainerDto = trainerDto.getUser().getFirstName();
        when(userNameAndPasswordChecker.isNullOrBlank(any(String.class), any(String.class))).thenReturn(false);
        when(trainerService.authenticate(selectedUserNameForTrainerDto, passwordForUser)).thenReturn(true);
        when(trainerService.select(selectedUserNameForTrainerDto)).thenReturn(trainerDto);

        trainerFacade.select(selectedUserNameForTrainerDto, passwordForUser);

        verify(trainerService, times(1)).authenticate(any(String.class), any(String.class));
        verify(trainerService, times(1)).authenticate(any(String.class), any(String.class));
        verify(trainerService, times(1)).select(userNameForTrainerDto);
    }

    @Test
    void selectTrainerNotFound() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(trainerService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);
        when(trainerService.select(userNameNotFound)).thenReturn(trainerDto);

        trainerFacade.select(userNameNotFound, passwordForUser);

        assertDoesNotThrow(() -> trainerService.select(userNameNotFound));
        verify(trainerService, times(1)).authenticate(any(String.class), any(String.class));
        verify(trainerService, times(1)).select(userNameNotFound);
    }

    @Test
    void updateTrainerSuccessfully() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTrainerDto, passwordForUser)).thenReturn(false);
        when(userDtoValidator.validate(trainerDto.getUser())).thenReturn(true);
        when(trainerService.authenticate(userNameForTrainerDto, passwordForUser)).thenReturn(true);
        when(trainerService.update(userNameForTrainerDto, trainerDto)).thenReturn(trainerDto);

        trainerFacade.update(userNameForTrainerDto, passwordForUser, trainerDto);

        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(any(String.class), any(String.class));
        verify(userDtoValidator, times(1)).validate(trainerDto.getUser());
        verify(trainerService, times(1)).authenticate(userNameForTrainerDto, passwordForUser);
        verify(trainerService, times(1)).update(userNameForTrainerDto, trainerDto);
    }

    @Test
    void changeStatusStatusNullFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTrainerDto, passwordForUser)).thenReturn(false);
        TrainerDto createdTrainerDto = trainerFacade.changeStatus("aaa", "aaa", null);

        assertNull(createdTrainerDto);
        verify(userNameAndPasswordChecker, never()).isNullOrBlank(userNameForTrainerDto, passwordForUser);
        verify(trainerService, never()).changeStatus("aaa", null);
    }

    @Test
    void changeStatusUserNameNotFoundFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(trainerService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);

        TrainerDto createdTrainerDto = trainerFacade.changeStatus(userNameNotFound, passwordForUser, true);

        assertNull(createdTrainerDto);
        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(userNameNotFound, passwordForUser);
        verify(trainerService, times(1)).authenticate(userNameNotFound, passwordForUser);
        verify(trainerService, never()).changeStatus(userNameNotFound, true);
    }

    @Test
    void changeStatusSuccessfully() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTrainerDto, passwordForUser)).thenReturn(false);
        when(trainerService.authenticate(userNameForTrainerDto, passwordForUser)).thenReturn(true);

        trainerFacade.changeStatus(userNameForTrainerDto, passwordForUser, true);

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameForTrainerDto, passwordForUser);
        verify(trainerService, times(1))
                .authenticate(userNameForTrainerDto, passwordForUser);
        verify(trainerService, times(1))
                .changeStatus(userNameForTrainerDto, true);
    }

    @Test
    void authenticateSuccessfully() {
        String gotUserNameForTrainerDto = trainerDto.getUser().getFirstName();
        when(userNameAndPasswordChecker.isNullOrBlank(any(String.class), any(String.class))).thenReturn(false);
        when(trainerService.authenticate(any(String.class), any(String.class))).thenReturn(true);

        trainerFacade.authenticate(gotUserNameForTrainerDto, passwordForUser);

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(any(String.class), any(String.class));
        verify(trainerService, times(1))
                .authenticate(any(String.class), any(String.class));
    }

    @Test
    void authenticateNotSuccessfulNullOrBlankUserNameOrPassword() {
        when(userNameAndPasswordChecker.isNullOrBlank(any(String.class), any(String.class))).thenReturn(true);

        trainerFacade.authenticate("aaa", "aaa");

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(any(String.class), any(String.class));
        verify(trainerService, never()).authenticate(any(String.class), any(String.class));
    }

    @Test
    void authenticateNotSuccessfulUserNameNotFoundOrDoesntMatchWithPassword() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(trainerService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);
        trainerFacade.authenticate(userNameNotFound, passwordForUser);

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(trainerService, times(1))
                .authenticate(any(String.class), any(String.class));
    }

    @Test
    void changePasswordNewPasswordNullFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(null)).thenReturn(true);
        TrainerDto createdTrainerDto = trainerFacade.changePassword(
                userNameForTrainerDto, passwordForUser, null);

        assertNull(createdTrainerDto);
        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(null);
        verify(trainerService, never()).changePassword(userNameForTrainerDto, null);
    }

    @Test
    void changePasswordUserNameNotFoundFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(newPassword)).thenReturn(false);
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser))
                .thenReturn(false);
        when(trainerService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);

        TrainerDto createdTrainerDto = trainerFacade.changePassword(
                userNameNotFound, passwordForUser, newPassword);

        assertNull(createdTrainerDto);
        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(newPassword);
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(trainerService, times(1))
                .authenticate(userNameNotFound, passwordForUser);
        verify(trainerService, never()).changePassword(userNameNotFound, newPassword);
    }

    @Test
    void changePasswordSuccessfully() {
        when(userNameAndPasswordChecker.isNullOrBlank(newPassword)).thenReturn(false);
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTrainerDto, passwordForUser))
                .thenReturn(false);
        when(trainerService.authenticate(userNameForTrainerDto, passwordForUser)).thenReturn(true);

        trainerFacade.changePassword(userNameForTrainerDto, passwordForUser, newPassword);

        verify(userNameAndPasswordChecker, times(1)).isNullOrBlank(newPassword);
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameForTrainerDto, passwordForUser);
        verify(trainerService, times(1))
                .authenticate(userNameForTrainerDto, passwordForUser);
        verify(trainerService, times(1))
                .changePassword(userNameForTrainerDto, newPassword);
    }

    @Test
    void changeSpecializationNullFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTrainerDto, passwordForUser)).thenReturn(false);
        TrainerDto createdTrainerDto = trainerFacade.changeSpecialization(
                userNameForTrainerDto, passwordForUser, null);

        assertNull(createdTrainerDto);
        verify(userNameAndPasswordChecker, never()).isNullOrBlank(userNameForTrainerDto, passwordForUser);
        verify(trainerService, never()).changeSpecialization("aaa", null);
    }

    @Test
    void changeSpecializationFoundFail() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameNotFound, passwordForUser)).thenReturn(false);
        when(trainerService.authenticate(userNameNotFound, passwordForUser)).thenReturn(false);

        TrainerDto createdTrainerDto = trainerFacade.changeSpecialization(
                userNameNotFound, passwordForUser, trainingType);

        assertNull(createdTrainerDto);
        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameNotFound, passwordForUser);
        verify(trainerService, times(1))
                .authenticate(userNameNotFound, passwordForUser);
        verify(trainerService, never()).changeSpecialization(userNameNotFound, trainingType);
    }

    @Test
    void changeSpecializationSuccessfully() {
        when(userNameAndPasswordChecker.isNullOrBlank(userNameForTrainerDto, passwordForUser)).thenReturn(false);
        when(trainerService.authenticate(userNameForTrainerDto, passwordForUser)).thenReturn(true);

        trainerFacade.changeSpecialization(userNameForTrainerDto, passwordForUser, trainingType);

        verify(userNameAndPasswordChecker, times(1))
                .isNullOrBlank(userNameForTrainerDto, passwordForUser);
        verify(trainerService, times(1))
                .authenticate(userNameForTrainerDto, passwordForUser);
        verify(trainerService, times(1))
                .changeSpecialization(userNameForTrainerDto, trainingType);
    }
}
