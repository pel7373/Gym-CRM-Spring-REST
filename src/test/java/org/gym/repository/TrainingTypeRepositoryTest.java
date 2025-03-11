package org.gym.repository;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.entity.TrainingType;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebAppConfiguration
class TrainingTypeRepositoryTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    void getAll() {
        List<TrainingType> trainingTypeList = trainingTypeRepository.findAll();

        assertAll (
                () -> assertEquals(5, DataStorage.expectedTrainingTypeNamesList.size())
        );

        trainingTypeList
                .forEach(t ->
                        assertTrue(DataStorage.expectedTrainingTypeNamesList.contains(t.getTrainingTypeName()), String.format("trainingTypeList contains %s name", t.getTrainingTypeName())));
    }

    @Test
    void findTrainingTypeNotValidFail() {
        Optional<TrainingType> notValid = trainingTypeRepository.findByName("NotValid");
        assertEquals(Optional.empty(), notValid);
    }
}
