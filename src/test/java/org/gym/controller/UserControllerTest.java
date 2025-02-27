package org.gym.controller;

import org.gym.config.Config;
import org.gym.config.TestConfig;
import org.gym.controller.impl.UserControllerImpl;
import org.gym.service.UserService;
import org.gym.util.TransactionIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
//@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class, Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class UserControllerTest {

        private MockMvc mockMvc;

        @Mock
        private UserService userService;

        @Mock
        private TransactionIdGenerator transactionIdGenerator;

//        @Autowired
//        private TransactionIdGenerator transactionIdGenerator;

        private UserController userController;

        @BeforeEach
        void setup() {
            userController = new UserControllerImpl(userService, transactionIdGenerator);
            mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        }

        @Test
        void test() throws Exception {
            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }
}
