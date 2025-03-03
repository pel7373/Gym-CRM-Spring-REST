package org.gym.service;

import org.gym.DataStorage;
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

import static org.gym.config.Config.ACCESS_DENIED_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final DataStorage ds = new DataStorage();

    @Test
    void changeStatusSuccessfully() {
        boolean statusInitial = ds.user.getIsActive();
        when(userRepository.findByUserName(ds.user.getUserName())).thenReturn(Optional.of(ds.user));

        boolean status = userService.changeStatus(ds.user.getUserName());

        assertEquals(ds.user.getIsActive(), status);
        verify(userRepository, times(1)).findByUserName(ds.user.getUserName());

        ds.user.setIsActive(statusInitial);
    }

    @Test
    void changePasswordSuccessfully() {
        String oldPassword = ds.user.getPassword();
        when(userRepository.findByUserName(ds.changeLoginRequest.getUserName())).thenReturn(Optional.of(ds.user));

        assertDoesNotThrow(() -> userService.changePassword(ds.changeLoginRequest));
        verify(userRepository, times(2)).findByUserName(ds.user.getUserName());
        ds.user.setPassword(oldPassword);
    }

    @Test
    void changePasswordFail() {
        when(userRepository.findByUserName(ds.user.getUserName()))
                .thenThrow(new EntityNotFoundException(ds.exceptionMessageNotFound));

        assertThrows(AccessDeniedException.class, () -> userService.changePassword(ds.changeLoginRequest),
                String.format(ACCESS_DENIED_EXCEPTION, ds.changeLoginRequest.getUserName()));
        verify(userRepository, times(1)).findByUserName(ds.user.getUserName());
    }

    @Test
    void authenticateSuccessfully() {
        when(userRepository.findByUserName(ds.user.getUserName())).thenReturn(Optional.of(ds.user));

        boolean result = userService.authenticate(ds.changeLoginRequest.getUserName(), ds.changeLoginRequest.getOldPassword());

        assertTrue(result);
        verify(userRepository, times(1)).findByUserName(ds.user.getUserName());
    }

    @Test
    void authenticateNotValidPasswordNotSuccessful() {
        when(userRepository.findByUserName(ds.user.getUserName())).thenReturn(Optional.of(ds.user));

        boolean isAuthenticate = userService.authenticate(ds.user.getUserName(), "NotValidPassword");

        assertFalse(isAuthenticate);
        verify(userRepository, times(1)).findByUserName(ds.user.getUserName());
    }
}
