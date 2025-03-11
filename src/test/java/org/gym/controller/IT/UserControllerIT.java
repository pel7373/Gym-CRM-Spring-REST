package org.gym.controller.IT;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.config.TestConfig;
import org.gym.controller.TraineeController;
import org.gym.controller.UserController;
import org.gym.entity.Trainee;
import org.gym.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
@ActiveProfiles("test")
class UserControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserController userController;

    @Autowired
    private TraineeController traineeController;

    @Autowired
    private TraineeRepository traineeRepository;

    String userName = DataStorage.changeLoginRequest.getUserName();
    String password = DataStorage.changeLoginRequest.getOldPassword();

    @BeforeEach
    void setUp() {
        traineeController.create(DataStorage.traineeDto);
    }

    @Test
    void changePasswordSuccessfully() {
        userController.changeLogin(DataStorage.changeLoginRequest);
        Trainee trainee = traineeRepository.findByUserName(DataStorage.changeLoginRequest.getUserName()).get();
        assertNotNull(trainee);
        assertEquals(DataStorage.changeLoginRequest.getNewPassword(), trainee.getUser().getPassword());
    }

    @Test
    void changeStatusSuccessfully() {
        boolean oldStatus = traineeRepository.findByUserName(DataStorage.traineeDto.getUser().getUserName()).get().getUser().getIsActive();
        boolean result = userController.changeStatus(DataStorage.traineeDto.getUser().getUserName());
        boolean newStatus = traineeRepository.findByUserName(DataStorage.traineeDto.getUser().getUserName()).get().getUser().getIsActive();
        assertAll(
                () -> assertEquals(newStatus, result),
                () -> assertEquals(!oldStatus, newStatus),
                () -> assertEquals(result, newStatus)
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
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
