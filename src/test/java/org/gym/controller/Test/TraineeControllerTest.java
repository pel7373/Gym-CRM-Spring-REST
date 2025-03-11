package org.gym.controller.Test;

import org.gym.controller.impl.TraineeControllerImpl;
import org.gym.dto.TraineeDto;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.service.TraineeService;
import org.gym.util.TransactionIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TraineeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private TraineeControllerImpl traineeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
        when(transactionIdGenerator.generate()).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void createTrainee() throws Exception {
        CreateResponse response = CreateResponse.builder().build();

        when(traineeService.create(any(TraineeDto.class))).thenReturn(response);

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


    @Test
    void selectTrainee() throws Exception {
        String userName = "trainee";
        TraineeSelectResponse response = new TraineeSelectResponse();
        when(traineeService.select(userName)).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainees/{username}", userName))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateTrainee() throws Exception {
        String userName = "trainee";
        TraineeUpdateResponse response = new TraineeUpdateResponse();
        when(traineeService.update(any(String.class), any(TraineeUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/trainees/{username}", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                      "user": {
                                              "firstName": "Ivan",
                                              "lastName": "Labunenko",
                                              "isActive": true
                                          },
                                          "dateOfBirth": "2000-01-01",
                                          "address": "Vinnitsya, Soborna str."
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTrainee() throws Exception {
        String userName = "trainee";
        doNothing().when(traineeService).delete(userName);

        mockMvc.perform(delete("/api/v1/trainees/{username}", userName))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUnassignedTrainers() throws Exception {
        String userName = "trainee";
        List<TrainerForListResponse> response = List.of(new TrainerForListResponse());

        when(traineeService.getUnassignedTrainersList(userName)).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainees/{username}/unassigned", userName))
                .andExpect(status().isOk());
    }

    @Test
    void updateTrainersList() throws Exception {
        String userName = "trainee";
        List<TrainerForListResponse> response = List.of(new TrainerForListResponse());

        when(traineeService.updateTrainersList(userName, List.of("Maria.Petrenko"))).thenReturn(response);

        mockMvc.perform(put("/api/v1/trainees/{username}/trainers", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                [
                                    "Maria.Petrenko"
                                ]
                                """))
                .andExpect(status().isOk());
    }
}
