package org.gym.controller.Test;

import org.gym.controller.impl.TrainingControllerImpl;
import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;
import org.gym.service.TrainingService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private TrainingControllerImpl trainingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
        when(transactionIdGenerator.generate()).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void testAddTraining() throws Exception {
        doNothing().when(trainingService).create(any(TrainingAddRequest.class));

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                     "traineeUserName": "Ivan.Ivanenko",
                                     "trainerUserName": "Maria.Petrenko",
                                     "trainingName": "Next zumba training",
                                     "trainingType": {
                                       "trainingTypeName": "Zumba"
                                     },
                                     "date": "2026-02-25",
                                     "duration": 45
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetTraineeTrainings() throws Exception {
        List<TraineeTrainingsListResponse> response = List.of(new TraineeTrainingsListResponse());

        when(trainingService.getTraineeTrainingsListCriteria(any(TraineeTrainingsDto.class))).thenReturn(response);
        mockMvc.perform(get("/api/v1/trainings/trainee")
                        .param("traineeUserName", "Ivan.Ivanenko")
                        .param("fromDate", "2025-01-01")
                        .param("toDate", "2026-04-29")
                        .param("trainerUserName", "Maria.Petrenko")
                        .param("trainingType", "Zumba"))
                        .andExpect(status().isOk());
    }

    @Test
    void testGetTrainerTrainings() throws Exception {
        List<TrainerTrainingsListResponse> response = List.of(new TrainerTrainingsListResponse());

        when(trainingService.getTrainerTrainingsListCriteria(any(TrainerTrainingsDto.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/trainings/trainer")
                        .param("trainerUserName", "Maria.Petrenko")
                        .param("fromDate", "2025-01-01")
                        .param("toDate", "2026-04-30")
                        .param("traineeUserName", "Ivan.Ivanenko"))
                .andExpect(status().isOk());
    }
}
