package org.gym.service;

import org.gym.Main;
import org.gym.exception.NullEntityException;
import org.gym.repository.UserRepository;
import org.gym.service.impl.UserNameGeneratorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Main.class)
class UserNameGeneratorServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserNameGeneratorServiceImpl userNameGeneratorService;

    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateUserNameWithoutSuffix() {
        when(userRepository.isExistsByUserName("Maria.Ivanenko")).thenReturn(false);

        String firstName = "Maria";
        String lastName = "Ivanenko";
        String expectedResult = "Maria.Ivanenko";
        String actualResult = userNameGeneratorService.generate(firstName, lastName);

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void generateUserNameWithSuffix() {
        when(userRepository.isExistsByUserName("John.Doe")).thenReturn(true);

        when(userRepository.isExistsByUserName("John.Doe0")).thenReturn(false);

        String firstName = "John";
        String lastName = "Doe";
        String expectedResult = "John.Doe0";
        String actualResult = userNameGeneratorService.generate(firstName, lastName);

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void generateUserNameIfFirstNameNull() {
        String firstName = null;
        String lastName = "Doe";

        assertThrows(NullEntityException.class, () -> userNameGeneratorService.generate(firstName, lastName),
                "generate: firstName or/and lastName can't be null, blank or empty");
        verify(userRepository, never()).isExistsByUserName(any(String.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void generateUserNameIfFirstNameEmptyOrBlank(String firstName) {
        String lastName = "Doe";

        assertThrows(NullEntityException.class, () -> userNameGeneratorService.generate(firstName, lastName),
                "generate: firstName or/and lastName can't be null, blank or empty");
        verify(userRepository, never()).isExistsByUserName(any(String.class));
    }

    @Test
    void generateUserNameIfLastNameNull() {
        String firstName = "John";
        String lastName = null;

        assertThrows(NullEntityException.class, () -> userNameGeneratorService.generate(firstName, lastName),
                "generate: firstName or/and lastName can't be null, blank or empty");
        verify(userRepository, never()).isExistsByUserName(any(String.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void generateUserNameIfLastNameEmptyOrBlank(String lastName) {
        String firstName = "John";

        assertThrows(NullEntityException.class, () -> userNameGeneratorService.generate(firstName, lastName),
                "generate: firstName or/and lastName can't be null, blank or empty");
        verify(userRepository, never()).isExistsByUserName(any(String.class));
    }
}
