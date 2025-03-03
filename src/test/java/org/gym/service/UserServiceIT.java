package org.gym.service;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.dto.response.CreateResponse;
import org.gym.entity.Trainee;
import org.gym.repository.TraineeRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebAppConfiguration
public class UserServiceIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private UserService userService;

    @Autowired
    private TraineeRepository traineeRepository;

    private final DataStorage ds = new DataStorage();
    private String userNameForTrainee;

    @Test
    void changeStatusSuccessfully() {
        CreateResponse createResponse = traineeService.create(ds.traineeDto);
        userNameForTrainee = createResponse.getUserName();

        assertAll(
                () -> assertNotNull(createResponse),
                () -> assertNotNull(userNameForTrainee)
        );

        Trainee createdTrainee = traineeRepository.findByUserName(userNameForTrainee).get();
        boolean oldStatus = createdTrainee.getUser().getIsActive();
        boolean status = userService.changeStatus(userNameForTrainee);

        assertEquals(!oldStatus, status);
    }


    @Test
    void changePasswordSuccessfully() {
        CreateResponse createResponse = traineeService.create(ds.traineeDto);
        userNameForTrainee = createResponse.getUserName();

        assertAll(
                () -> assertNotNull(createResponse),
                () -> assertNotNull(userNameForTrainee)
        );

        userService.changePassword(ds.changeLoginRequest);
        Trainee changedTrainee = traineeRepository.findByUserName(userNameForTrainee).get();

        String changedPassword = changedTrainee.getUser().getPassword();

        assertAll(
                () -> assertNotNull(changedTrainee),
                () -> assertNotNull(changedTrainee.getUser()),
                () -> assertEquals(ds.changeLoginRequest.getNewPassword(), changedPassword)
        );
    }

    @Test
    void authenticateSuccessfully() {
        CreateResponse createResponse = traineeService.create(ds.traineeDto);
        userNameForTrainee = createResponse.getUserName();

        boolean result = userService.authenticate(createResponse.getUserName(), createResponse.getPassword());

        assertAll(
                () -> assertNotNull(createResponse),
                () -> assertNotNull(createResponse.getUserName()),
                () -> assertTrue(result)
        );
    }

    @Test
    void authenticateNotFoundFail() {
        boolean result = userService.authenticate("aaaa", "aaaa");
        assertFalse(result);
    }

    @Test
    void authenticateNotValidPasswordFail() {
        CreateResponse createResponse = traineeService.create(ds.traineeDto);
        userNameForTrainee = createResponse.getUserName();

        boolean result = userService.authenticate(createResponse.getUserName(), "NotValidPassword");

        assertAll(
                () -> assertNotNull(createResponse),
                () -> assertNotNull(createResponse.getUserName()),
                () -> assertFalse(result)
        );
    }
}
