package org.gym.mapper;

import org.gym.config.TestConfig;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.entity.User;
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
class TrainerMapperTest {

    @Autowired
    private TrainerMapper trainerMapper;

    @Test
    void convertToDto() {
        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("Maria")
                        .lastName("Petrenko")
                        .userName("Maria.Petrenko")
                        .password("password")
                        .isActive(true)
                        .build())
                .specialization(TrainingType.builder().id(2L).trainingTypeName("yoga").build())
                .build();

        TrainerDto createdTrainerDto = trainerMapper.convertToDto(trainer);

        assertNotNull(createdTrainerDto);
        assertAll(
                "Grouped assertions of created trainerDto",
                () -> assertEquals(trainer.getUser().getFirstName(), createdTrainerDto.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), createdTrainerDto.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainer.getUser().getUserName(), createdTrainerDto.getUser().getUserName(), "check userName"),
                () -> assertEquals(trainer.getUser().getIsActive(), createdTrainerDto.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), createdTrainerDto.getSpecialization().getTrainingTypeName(), "check specialization")
        );
    }

    @Test
    void convertToDtoWithNullTrainer() {
        TrainerDto trainerDto = trainerMapper.convertToDto(null);
        assertNull(trainerDto, "ConvertToDto: null when input is null");
    }

    @Test
    void convertToEntity() {
        TrainerDto trainerDto = TrainerDto.builder()
                .user(UserDto.builder()
                        .firstName("Maria")
                        .lastName("Petrenko")
                        .userName("Maria.Petrenko")
                        .isActive(true)
                        .build())
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("yoga")
                        .build())
                .build();

        Trainer createdTrainer = trainerMapper.convertToEntity(trainerDto);

        assertNotNull(createdTrainer);
        assertAll(
                "Grouped assertions of created trainerDto",
                () -> assertEquals(trainerDto.getUser().getFirstName(), createdTrainer.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainerDto.getUser().getLastName(), createdTrainer.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainerDto.getUser().getUserName(), createdTrainer.getUser().getUserName(), "check userName"),
                () -> assertEquals(trainerDto.getUser().getIsActive(), createdTrainer.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainerDto.getSpecialization().getTrainingTypeName(), createdTrainer.getSpecialization().getTrainingTypeName(), "check specialization")
        );
    }

    @Test
    void convertToEntityWithNullTrainerDto() {
        Trainer trainer = trainerMapper.convertToEntity(null);
        assertNull(trainer, "ConvertToEntity: null when input is null");
    }
}
