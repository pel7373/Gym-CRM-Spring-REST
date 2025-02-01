package org.gym.mapper;

import org.gym.config.TestConfig;
import org.gym.dto.TrainingTypeDto;
import org.gym.entity.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainingTypeMapperTest {

    @Autowired
    private TrainingTypeMapper trainingTypeMapper;

    @Test
    void convertToDto() {

        TrainingType trainingType = TrainingType.builder()
                .trainingTypeName("Yoga")
                .build();

        TrainingTypeDto trainingTypeDto = trainingTypeMapper.convertToDto(trainingType);

        assertNotNull(trainingTypeDto);
        assertEquals(trainingType.getTrainingTypeName(), trainingTypeDto.getTrainingTypeName());
    }

    @Test
    void convertToDtoWithNullTrainee() {
        TrainingTypeDto trainingTypeDto = trainingTypeMapper.convertToDto(null);
        assertNull(trainingTypeDto, "ConvertToDto: null when input is null");
    }

    @Test
    void convertToEntity() {
        TrainingTypeDto trainingTypeDto = TrainingTypeDto.builder()
                .trainingTypeName("Yoga")
                .build();

        TrainingType trainingType = trainingTypeMapper.convertToEntity(trainingTypeDto);

        assertNotNull(trainingType);
        assertEquals(trainingTypeDto.getTrainingTypeName(), trainingType.getTrainingTypeName());
    }

    @Test
    void convertToEntityWithNullTraineeDto() {
        TrainingType trainingType = trainingTypeMapper.convertToEntity(null);
        assertNull(trainingType, "ConvertToEntity: null when input is null");
    }
}
