package org.gym.service.Test;

import org.gym.DataStorage;
import org.gym.dto.response.trainingtype.TrainingTypeResponse;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void getAll() {
        when(trainingTypeRepository.findAll()).thenReturn(DataStorage.trainingTypeList);

        List<TrainingTypeResponse> trainingTypeResponseList = trainingTypeService.findAll();

        assertEquals(DataStorage.expectedTrainingTypeNamesList.size(), trainingTypeResponseList.size());

        trainingTypeResponseList
                .forEach(t ->
                        assertTrue(DataStorage.expectedTrainingTypeNamesList.contains(t.getTrainingTypeName()), String.format("trainingTypeList contains %s name", t.getTrainingTypeName())));
    }
}
