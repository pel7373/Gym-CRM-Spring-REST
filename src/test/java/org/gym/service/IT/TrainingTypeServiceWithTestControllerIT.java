package org.gym.service.IT;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.dto.response.trainingType.TrainingTypeResponse;
import org.gym.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@ActiveProfiles("prod")
@WebAppConfiguration
public class TrainingTypeServiceWithTestControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrainingTypeService trainingTypeService;

    private final DataStorage ds = new DataStorage();

    @Test
    void getAll() {
        List<TrainingTypeResponse> trainingTypeResponseList = trainingTypeService.findAll();

        assertAll (
                () -> assertEquals(5, trainingTypeResponseList.size()),
                () -> assertEquals(5, ds.expectedTrainingTypeNamesList.size())
        );

        trainingTypeResponseList
                .forEach(t ->
                        assertTrue(ds.expectedTrainingTypeNamesList.contains(t.getTrainingTypeName()), String.format("trainingTypeList contains %s name", t.getTrainingTypeName())));
    }
}
