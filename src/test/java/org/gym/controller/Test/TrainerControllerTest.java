package org.gym.controller.Test;

import org.gym.DataStorage;
import org.gym.controller.impl.TraineeControllerImpl;
import org.gym.controller.impl.TrainerControllerImpl;
import org.gym.dto.TrainerDto;
import org.gym.dto.request.trainer.TrainerCreateRequest;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.service.TraineeService;
import org.gym.service.TrainerService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private TrainerControllerImpl trainerController;

    private final DataStorage ds = new DataStorage();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
        when(transactionIdGenerator.generate()).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void createTrainer() throws Exception {
        CreateResponse response = CreateResponse.builder().build();

        when(trainerService.create(any(TrainerDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "user": {
                                            "firstName": "Maria",
                                            "lastName": "Petrenko"
                                        },
                                        "specialization": {
                                            "trainingTypeName": "Zumba"
                                        }
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void selectTrainer() throws Exception {
        TrainerSelectResponse response = TrainerSelectResponse.builder().build();

        when(trainerService.select("trainer123")).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainers/trainer123"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateTrainer() throws Exception {
        String userName = "trainer";
        TrainerUpdateResponse response = TrainerUpdateResponse.builder().build();

        when(trainerService.update(any(String.class), any(TrainerUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/trainers/{username}", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                      "user": {
                                              "firstName": "Maria",
                                              "lastName": "Fedorenko",
                                              "isActive": false
                                          },
                                          "specialization": {
                                              "trainingTypeName": "resistae"
                                          }
                                }
                                """))
                .andExpect(status().isOk());
    }
}
