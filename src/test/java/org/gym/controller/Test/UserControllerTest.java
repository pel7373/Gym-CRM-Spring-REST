package org.gym.controller.Test;

import jakarta.transaction.Transactional;
import org.gym.DataStorage;
import org.gym.controller.UserController;
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

    private final DataStorage ds = new DataStorage();

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

        doNothing().when(userService).changePassword(ds.changeLoginRequest);
        when(userService.authenticate(ds.changeLoginRequest.getUserName(), ds.changeLoginRequest.getOldPassword()))
                .thenReturn(true);

        mockMvc.perform(put("/api/v1/password", ds.changeLoginRequest))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateSuccess() throws Exception {
        String userName = "user";
        String password = "password";

        when(userService.authenticate(userName, password)).thenReturn(true);

        mockMvc.perform(get("/api/v1/{username}/{password}", userName, password))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateFailure() throws Exception {
        String userName = "user";
        String password = "password";

        when(userService.authenticate(userName, password)).thenReturn(false);

        mockMvc.perform(get("/api/v1/users/authenticate", userName, password))
                .andExpect(status().isNotFound());
    }
}
