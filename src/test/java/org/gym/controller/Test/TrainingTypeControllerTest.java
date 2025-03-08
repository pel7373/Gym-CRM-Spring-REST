package org.gym.controller.Test;

import org.gym.controller.impl.TrainingTypeControllerImpl;
import org.gym.dto.response.trainingType.TrainingTypeResponse;
import org.gym.service.TrainingTypeService;
import org.gym.util.TransactionIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingTypeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private TrainingTypeControllerImpl trainingTypeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingTypeController).build();
        when(transactionIdGenerator.generate()).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void testGetTraineeTrainings() throws Exception {
        List<TrainingTypeResponse> response = List.of(new TrainingTypeResponse());
        when(trainingTypeService.findAll()).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainingtypes"))
                .andExpect(status().isOk());
    }
}
