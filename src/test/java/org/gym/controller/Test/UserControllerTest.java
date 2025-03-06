package org.gym.controller.Test;

import org.gym.controller.impl.UserControllerImpl;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.service.UserService;
import org.gym.util.TransactionIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private UserControllerImpl userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        when(transactionIdGenerator.generate()).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void changeStatus() throws Exception {
        String userName = "user";

        when(userService.changeStatus(userName)).thenReturn(true);

        mockMvc.perform(patch("/api/v1/{username}/status", userName))
                .andExpect(status().isOk());
    }

    @Test
    void changePassword() throws Exception {
        doNothing().when(userService).changePassword(any(ChangeLoginRequest.class));
        when(userService.authenticate(any(String.class), any(String.class)))
                .thenReturn(true);

        mockMvc.perform(put("/api/v1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userName": "Ivan.Ivanenko",
                                        "oldPassword": "12345",
                                        "newPassword": "123456"
                                }
                                """))

                .andExpect(status().isOk());
    }

    @Test
    void authenticateSuccess() throws Exception {
        String userName = "user";
        String password = "password";

        when(userService.authenticate(userName, password)).thenReturn(true);

        mockMvc.perform(get("/api/v1/login/{username}/{password}", userName, password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateFailure() throws Exception {
        String userName = "user";
        String password = "password";

        when(userService.authenticate(userName, password)).thenReturn(false);

        mockMvc.perform(get("/api/v1/login", userName, password))
                .andExpect(status().isNotFound());
    }
}
