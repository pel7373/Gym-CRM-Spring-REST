package org.gym.controller;

import lombok.RequiredArgsConstructor;
import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.config.TestConfig;
import org.gym.config.WebConfig;
import org.gym.entity.Trainee;
import org.gym.entity.User;
import org.gym.repository.TraineeRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
@ActiveProfiles("test")
public class UserControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserController userController;

    @Autowired
    private TraineeController traineeController;

    @Autowired
    private TraineeRepository traineeRepository;

    private final DataStorage ds = new DataStorage();
    String userName = ds.changeLoginRequest.getUserName();
    String password = ds.changeLoginRequest.getOldPassword();

    @BeforeEach
    void setUp() {
        traineeController.create(ds.traineeDto);
    }

    @Test
    void changePasswordSuccessfully() {
        userController.changeLogin(ds.changeLoginRequest);
        Trainee trainee = traineeRepository.findByUserName(ds.changeLoginRequest.getUserName()).get();
        assertNotNull(trainee);
        assertEquals(ds.changeLoginRequest.getNewPassword(), trainee.getUser().getPassword());
    }

    @Test
    void changeStatusSuccessfully() {
        boolean newStatus = false;
        userController.changeStatus(ds.userNameForTrainerDto);
        Trainee trainee = traineeRepository.findByUserName(ds.userNameForTrainerDto).get();
        assertAll(
                () -> assertEquals(newStatus, trainee.getUser().getIsActive()),
                () -> assertEquals(ds.userNameForTrainerDto, trainee.getUser().getUserName())
        );
    }

    @Test
    void loginSuccessfully() {
        ResponseEntity<Void> response = userController.login(userName, password);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void loginNotValidPasswordFail() {
        String notValidPassword = "notValidPassword";
        ResponseEntity<Void> response = userController.login(userName, notValidPassword);
        assertEquals(ResponseEntity.badRequest().build(), response.getStatusCode());
    }
}
