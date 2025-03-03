package org.gym.service;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.dto.response.CreateResponse;
import org.gym.entity.Trainee;
import org.gym.repository.TraineeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@ActiveProfiles("prod")
@WebAppConfiguration
public class UserServiceWithTestContainersIT {

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

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQL10Dialect");
        registry.add("hibernate.hbm2ddl.auto", () -> "create");
        registry.add("hibernate.show_sql", () -> true);
        registry.add("hibernate.format_sql", () -> true);
        registry.add("hibernate.jdbc.lob.non_contextual_creation", () -> true);
    }

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
