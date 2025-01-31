package org.gym.facade;

import org.gym.facade.impl.UserNameAndPasswordChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserNameAndPasswordCheckerTest {

    @InjectMocks
    private UserNameAndPasswordChecker userNameAndPasswordChecker;

    @Test
    void isNullOrBlankUserNameAndPasswordNotNullOrBlank() {
        String userName = "aaa";
        String password = "bbb";
        boolean result = userNameAndPasswordChecker.isNullOrBlank(userName, password);
        assertFalse(result);
    }

    private static Stream<Arguments> provideStringsForTest() {
        return Stream.of(
                Arguments.of("alex", ""),
                Arguments.of("", "alex"),
                Arguments.of("", ""),
                Arguments.of("   ", "   "),
                Arguments.of(null, null),
                Arguments.of(null, "aaaa"),
                Arguments.of("aaaa", null),
                Arguments.of(null, null),
                Arguments.of("alex", "   ")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTest")
    void isNullOrBlankUserNameOrAndPassword(String userName, String password) {
        boolean result = userNameAndPasswordChecker.isNullOrBlank(userName, password);
        assertTrue(result);
    }

    @Test
    void isNullOrBlankUserNameNull() {
        String userName = null;
        boolean result = userNameAndPasswordChecker.isNullOrBlank(userName);
        assertTrue(result);
    }

    @Test
    void isNullOrBlankUserNameBlank() {
        String userName = "  ";
        boolean result = userNameAndPasswordChecker.isNullOrBlank(userName);
        assertTrue(result);
    }

    @Test
    void isNullOrBlankUserNameNotNullOrBlank() {
        String userName = "aaaa";
        boolean result = userNameAndPasswordChecker.isNullOrBlank(userName);
        assertFalse(result);
    }
}
