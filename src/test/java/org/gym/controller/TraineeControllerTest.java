package org.gym.controller;

import jakarta.transaction.Transactional;
import org.gym.DataStorage;
import org.gym.controller.impl.TraineeControllerImpl;
import org.gym.dto.TraineeDto;
import org.gym.dto.response.CreateResponse;
import org.gym.service.TraineeService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TraineeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private TraineeControllerImpl traineeController;

    private final DataStorage ds = new DataStorage();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
        when(transactionIdGenerator.generate()).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void testCreateTrainee() throws Exception {

        when(traineeService.create(any(TraineeDto.class))).thenReturn(ds.traineeCreateResponse);

        mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "user": {
                                        "firstName": "Maria",
                                        "lastName": "Petrenko"
                                    },
                                    "dateOfBirth": "1995-01-23",
                                    "address": "Vinnitsya, Soborna str. 35, ap. 26"
                                }
                                """))
                .andExpect(status().isCreated());
    }
}
