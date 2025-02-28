package org.gym.controller;

import jakarta.transaction.Transactional;
import org.gym.config.Config;
import org.gym.config.TestConfig;
import org.gym.controller.impl.UserControllerImpl;
import org.gym.service.UserService;
import org.gym.util.TransactionIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(SpringExtension.class)
////@ExtendWith(MockitoExtension.class)
//@ContextConfiguration(classes = {TestConfig.class, Config.class})
//@TestPropertySource(locations = "classpath:application-test.properties")
//@ActiveProfiles("test")
@Transactional
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private UserControllerImpl userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        when(transactionIdGenerator.generate()).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void testChangeStatus() throws Exception {
        String userName = "User";

        when(userService.changeStatus(userName)).thenReturn(true);

        mockMvc.perform(patch("/api/v1/{username}/status", userName))
                .andExpect(status().isOk());
    }
}
