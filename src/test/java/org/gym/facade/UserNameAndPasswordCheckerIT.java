package org.gym.facade;

import org.gym.config.Config;
import org.gym.facade.impl.UserNameAndPasswordChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@jakarta.transaction.Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class UserNameAndPasswordCheckerIT {

    @Autowired
    private UserNameAndPasswordChecker userNameAndPasswordChecker;

    @Test
    void isNullOrBlankUserNameAndPasswordNotNullOrBlank() {
        String userName = "aaa";
        String password = "bbb";
        boolean result = userNameAndPasswordChecker.isNullOrBlank(userName, password);
        assertFalse(result);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "alex, ''",
            "'', alex",
            "'', ''",
            "'   ', '   '",
            "alex, ' '"
    })
    void isNullOrBlankPasswordBlank(String userName, String password) {
        boolean result = userNameAndPasswordChecker.isNullOrBlank(userName, password);
        assertTrue(result);
    }

    @Test
    void isNullOrBlankPasswordNull() {
        String userName = "aaa";
        String password = null;
        boolean result = userNameAndPasswordChecker.isNullOrBlank(userName, password);
        assertTrue(result);
    }

    @Test
    void isNullOrBlankUserNameAndPasswordNull() {
        String userName = null;
        String password = null;
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
