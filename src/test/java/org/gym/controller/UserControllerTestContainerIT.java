package org.gym.controller;

import org.gym.DataStorage;
import org.gym.config.Config;

import org.gym.config.TestConfig;
import org.gym.config.WebConfig;
import org.gym.controller.impl.UserControllerImpl;
import org.gym.entity.Trainee;
import org.gym.entity.User;
import org.gym.repository.TraineeRepository;
import org.gym.repository.UserRepository;
import org.gym.repository.impl.UserRepositoryImpl;
import org.gym.service.UserService;
import org.gym.service.impl.TraineeServiceImpl;
import org.gym.service.impl.UserServiceImpl;
import org.gym.util.TransactionIdGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@ActiveProfiles("prod")
public class UserControllerTestContainerIT {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TransactionIdGenerator transactionIdGenerator;

    private UserServiceImpl userService;
    private MockMvc mockMvc;
    private static PostgreSQLContainer<?> postgres;
    private static DataSource dataSource;

    private final DataStorage ds = new DataStorage();
    String userName = ds.changeLoginRequest.getUserName();
    String password = ds.changeLoginRequest.getOldPassword();
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    {
        //traineeRepository.save(ds.trainee1);
    }

    @BeforeAll
    static void startContainer() {
        postgres = new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        postgres.start();

        dataSource = new DriverManagerDataSource(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );


    }

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
        UserController userController = new UserControllerImpl(userService, transactionIdGenerator);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        //userController.login(ds.traineeDto);
    }
    @Test
    void changeStatusSuccessfully() {
        //boolean newStatus = false;
        boolean statusBefore = userRepository.findByUserName(ds.userNameForTrainerDto).get().getIsActive();
        userController.changeStatus(ds.userNameForTrainerDto);
        boolean statusAfter = userRepository.findByUserName(ds.userNameForTrainerDto).get().getIsActive();;
        assertAll(
                () -> assertNotEquals(statusBefore, statusAfter)
                );
    }

//    @Test
//    void changePasswordSuccessfully() {
//        userController.changeLogin(ds.changeLoginRequest);
//        Trainee trainee = traineeRepository.findByUserName(ds.changeLoginRequest.getUserName()).get();
//        assertNotNull(trainee);
//        assertEquals(ds.changeLoginRequest.getNewPassword(), trainee.getUser().getPassword());
//    }
//


//    @Test
//    void loginSuccessfully() {
//        ResponseEntity<Void> response = userController.login(userName, password);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void loginNotValidPasswordFail() {
//        String notValidPassword = "notValidPassword";
//        ResponseEntity<Void> response = userController.login(userName, notValidPassword);
//        assertEquals(ResponseEntity.badRequest().build(), response.getStatusCode());
//    }
}
