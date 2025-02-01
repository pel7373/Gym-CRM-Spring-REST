package org.gym.validator;

import org.gym.config.Config;
import org.gym.dto.UserDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class UserDtoValidatorTest {

    @Autowired
    private UserDtoValidator userDtoValidator;

    static List<UserDto> validDto = Arrays.asList(
            new UserDto("Maria", "Petrenko", "Maria.Petrenko", true),
            new UserDto("Petro", "Ivanenko", "Maria.Petrenko", false)
    );

    @ParameterizedTest
    @FieldSource("validDto")
    void validateValidUserDtoSuccessfully(UserDto userDto) {
        boolean isValidUserDto = userDtoValidator.validate(userDto);
        String errorMessages = userDtoValidator.getErrorMessage(userDto);
        assertTrue(isValidUserDto);
        assertEquals("", errorMessages);
    }

    static List<UserDto> notValidDto = Arrays.asList(
            new UserDto(null, "Petrenko", "Maria.Petrenko", true),
            new UserDto("Ma", "Petrenko", "Maria.Petrenko", true),
            new UserDto("Maria", null, "Maria.Petrenko", false),
            new UserDto("Maria", "Pe", "Maria.Petrenko", true),
            new UserDto("Maria", "Petrenko", "Maria.Petrenko", null),
            new UserDto("Maria22", "Petrenko", "Maria.Petrenko", true),
            new UserDto("Maria", "Petrenko33", "Maria.Petrenko", true)
    );

    @ParameterizedTest
    @FieldSource("notValidDto")
    void validateNotValidUserDtoFail(UserDto userDto) {
        boolean isValidUserDto = userDtoValidator.validate(userDto);
        String errorMessages = userDtoValidator.getErrorMessage(userDto);
        assertFalse(isValidUserDto);
        assertNotNull(errorMessages);
    }
}
