package org.gym.repository;

import org.gym.config.Config;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainerRepositoryTest {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    private Trainer trainer;
    private final String userNameDoesntExist = "userNameDoesntExist";

    @BeforeEach
    void setUp()
    {
        String trainingTypeName = "Zumba";
        TrainingType trainingType = trainingTypeRepository.findByName(trainingTypeName).get();

        trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .userName("John.Doe")
                        .password("password")
                        .isActive(true)
                        .build())
                .specialization(trainingType)
                .build();
    }

    @Test
    void saveTrainerSuccessfully() {
        Trainer savedTrainer = trainerRepository.save(trainer);
        assertAll(
                () -> assertNotNull(savedTrainer),
                () -> assertEquals("John", savedTrainer.getUser().getFirstName()),
                () -> assertEquals(trainer.getUser().getUserName(), savedTrainer.getUser().getUserName())
        );
    }

    @Test
    void saveTrainerWithNullId() {
        trainer.setId(null);
        Trainer savedTrainer = trainerRepository.save(trainer);
        assertAll(
                () -> assertNotNull(savedTrainer),
                () -> assertNotNull(savedTrainer.getId())
        );
    }

    @Test
    void saveTrainerWithExistingId() {
        trainer.setId(2L);
        Trainer savedTrainer = trainerRepository.save(trainer);
        assertAll(
                () -> assertNotNull(savedTrainer),
                () -> assertEquals("John", savedTrainer.getUser().getFirstName()),
                () -> assertEquals(trainer.getUser().getUserName(), savedTrainer.getUser().getUserName())
        );
    }

    @Test
    void findByUserNameNoResult() {
        assertDoesNotThrow(() -> trainerRepository.findByUserName(userNameDoesntExist));
    }

    @Test
    void findByUserNameSuccess() {
        Trainer savedTrainer = trainerRepository.save(trainer);
        Optional<Trainer> foundTrainer =
                trainerRepository.findByUserName(savedTrainer.getUser().getUserName());
        assertAll(
                () -> assertTrue(foundTrainer.isPresent()),
                () -> assertEquals(savedTrainer.getUser().getFirstName(),
                        foundTrainer.get().getUser().getFirstName())
        );
    }
}
