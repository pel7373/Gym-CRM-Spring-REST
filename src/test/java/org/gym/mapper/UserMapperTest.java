package org.gym.mapper;

import org.gym.config.TestConfig;
import org.gym.dto.UserDto;
import org.gym.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void convertToDto() {
        User user = User.builder()
                .firstName("Maria")
                .lastName("Petrenko")
                .userName("Maria.Petrenko")
                .password("password")
                .isActive(true)
                .build();

        UserDto userDto = userMapper.convertToDto(user);

        assertNotNull(userDto);
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getUserName(), userDto.getUserName());
        assertEquals(user.getIsActive(), userDto.getIsActive());
    }

    @Test
    void convertToDtoWithNullTrainee() {
        UserDto userDto = userMapper.convertToDto(null);
        assertNull(userDto, "ConvertToDto: null when input is null");
    }

    @Test
    void convertToEntity() {
        UserDto userDto = UserDto.builder()
                .firstName("Maria")
                .lastName("Petrenko")
                .userName("Maria.Petrenko")
                .isActive(true)
                .build();

        User user = userMapper.convertToEntity(userDto);

        assertNotNull(user);
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getUserName(), user.getUserName());
        assertEquals(userDto.getIsActive(), user.getIsActive());

    }

    @Test
    void convertToEntityWithNullTraineeDto() {
        User user = userMapper.convertToEntity(null);
        assertNull(user, "ConvertToEntity: null when input is null");
    }
}
