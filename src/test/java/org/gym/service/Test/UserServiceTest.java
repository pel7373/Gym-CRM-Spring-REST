package org.gym.service.Test;

import org.gym.DataStorage;
import org.gym.entity.User;
import org.gym.exception.AccessDeniedException;
import org.gym.exception.EntityNotFoundException;
import org.gym.repository.UserRepository;
import org.gym.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.gym.config.Config.ACCESS_DENIED_EXCEPTION_MESSAGE_TEMPLATE;
import static org.gym.config.Config.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void changeStatusSuccessfullyWasNull() {
        User user = new User(null, "Ivan", "Ivanenko", "Ivan.Ivanenko", DataStorage.passwordForUser, null);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(any(User.class));

        boolean status = userService.changeStatus(user.getUserName());

        assertTrue(status);
        verify(userRepository, times(1)).findByUserName(user.getUserName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void changeStatusSuccessfullyStatusNotNull() {
        User user = new User(null, "Ivan", "Ivanenko", "Ivan.Ivanenko", DataStorage.passwordForUser, true);

        when(userRepository.findByUserName( user.getUserName())).thenReturn(Optional.of( user));
        when(userRepository.save(any(User.class))).thenReturn(any(User.class));

        boolean status = userService.changeStatus( user.getUserName());

        assertEquals( user.getIsActive(), status);
        verify(userRepository, times(1)).findByUserName(user.getUserName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void changeStatusNotFoundFail() {
        User user = new User(null, "Ivan", "Ivanenko", "Ivan.Ivanenko", DataStorage.passwordForUser, true);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.changeStatus(user.getUserName()),
                String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, user.getUserName()));

        verify(userRepository, times(1)).findByUserName(user.getUserName());
    }

    @Test
    void changePasswordSuccessfully() {
        User user = new User(null, "Ivan", "Ivanenko", "Ivan.Ivanenko", DataStorage.passwordForUser, null);

        when(userRepository.findByUserName(DataStorage.changeLoginRequest.getUserName())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.changePassword(DataStorage.changeLoginRequest));

        verify(userRepository, times(2)).findByUserName(DataStorage.changeLoginRequest.getUserName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePasswordFail() {
        when(userRepository.findByUserName(DataStorage.changeLoginRequest.getUserName()))
                .thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class, () -> userService.changePassword(DataStorage.changeLoginRequest),
                String.format(ACCESS_DENIED_EXCEPTION_MESSAGE_TEMPLATE, DataStorage.changeLoginRequest.getUserName()));
        verify(userRepository, times(1)).findByUserName(DataStorage.changeLoginRequest.getUserName());
    }

    @Test
    void authenticateSuccessfully() {
        when(userRepository.findByUserName(DataStorage.user.getUserName())).thenReturn(Optional.of(DataStorage.user));

        boolean result = userService.authenticate(DataStorage.user.getUserName(), DataStorage.user.getPassword());

        assertTrue(result);
        verify(userRepository, times(1)).findByUserName(DataStorage.user.getUserName());
    }

    @Test
    void authenticateNotValidPasswordNotSuccessful() {
        when(userRepository.findByUserName(DataStorage.user.getUserName())).thenReturn(Optional.of(DataStorage.user));

        boolean isAuthenticate = userService.authenticate(DataStorage.user.getUserName(), "NotValidPassword");

        assertFalse(isAuthenticate);
        verify(userRepository, times(1)).findByUserName(DataStorage.user.getUserName());
    }

    @Test
    void authenticateNotFoundFail() {
        when(userRepository.findByUserName(DataStorage.user.getUserName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.authenticate(DataStorage.user.getUserName(), "NotValidPassword"));

        verify(userRepository, times(1)).findByUserName(DataStorage.user.getUserName());
    }
}
