package org.gym.controller.IT;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.config.TestConfig;
import org.gym.controller.TrainingTypeController;
import org.gym.dto.response.trainingtype.TrainingTypeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
@ActiveProfiles("test")
class TrainingTypeControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrainingTypeController trainingTypeController;

    @Test
    void getAll() {
        List<TrainingTypeResponse> trainingTypeResponseList = trainingTypeController.getAll();

        assertEquals(DataStorage.expectedTrainingTypeNamesList.size(), trainingTypeResponseList.size());

        trainingTypeResponseList
                .forEach(t ->
                        assertTrue(DataStorage.expectedTrainingTypeNamesList.contains(t.getTrainingTypeName()), String.format("trainingTypeList contains %s name", t.getTrainingTypeName())));
    }
}
