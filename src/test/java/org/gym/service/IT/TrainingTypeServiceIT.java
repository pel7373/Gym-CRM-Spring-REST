package org.gym.service.IT;

import org.gym.DataStorage;
import org.gym.config.TestConfig;
import org.gym.dto.response.trainingType.TrainingTypeResponse;
import org.gym.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebAppConfiguration
class TrainingTypeServiceIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrainingTypeService trainingTypeService;

    private DataStorage ds = new DataStorage();

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
