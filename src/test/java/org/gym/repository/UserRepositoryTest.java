package org.gym.repository;

import org.gym.config.Config;
import org.gym.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebAppConfiguration
public class UserRepositoryTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private UserRepository userRepository;

    private final User user;
    private final String userNameDoesntExist = "userNameDoesntExist";

    {
        user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .userName("John.Doe")
                .isActive(true)
                .build();
    }

    @Test
    void saveUserSuccessfully() {
        User savedUser = userRepository.save(user);
        assertAll(
                () -> assertNotNull(savedUser),
                () -> assertEquals("John", savedUser.getFirstName()),
                () -> assertEquals(user.getUserName(), savedUser.getUserName())
        );
    }

    @Test
    void saveUserWithNullId() {
        user.setId(null);
        User savedUser = userRepository.save(user);
        assertAll(
                () -> assertNotNull(savedUser),
                () -> assertNotNull(savedUser.getId())
        );
    }

    @Test
    void saveUserWithExistingId() {
        user.setId(2L);
        User savedUser = userRepository.save(user);
        assertAll(
                () -> assertNotNull(savedUser),
                () -> assertEquals("John", savedUser.getFirstName()),
                () -> assertEquals(user.getUserName(), savedUser.getUserName())
        );
    }

    @Test
    void findByUserNameNoResult() {
        assertDoesNotThrow(() -> userRepository.findByUserName(userNameDoesntExist));
    }

    @Test
    void findByUserNameSuccess() {
        User savedUser = userRepository.save(user);
        Optional<User> foundUser =
                userRepository.findByUserName(savedUser.getUserName());
        assertAll(
                () -> assertTrue(foundUser.isPresent()),
                () -> assertEquals(savedUser.getFirstName(),
                        foundUser.get().getFirstName())
        );
    }
}
