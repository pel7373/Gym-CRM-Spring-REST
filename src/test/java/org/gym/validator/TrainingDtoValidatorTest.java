package org.gym.validator;

import org.gym.config.Config;
import org.gym.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainingDtoValidatorTest {

    @Autowired
    private TrainingDtoValidator trainingDtoValidator;

    @Test
    void validSuccessfully() {
        TrainingDto trainingDto = TrainingDto.builder().trainingName("Zumba next workout").date(LocalDate.now().plusDays(3)).duration(45).build();
        assertTrue(trainingDtoValidator.validate(trainingDto));
    }

    static List<TrainingDto> notValidDto = Arrays.asList(
            TrainingDto.builder().trainingName(null).date(LocalDate.now().plusDays(3)).duration(45).build(),
            TrainingDto.builder().trainingName("Zumba next workout").date(null).duration(7).build(),
            TrainingDto.builder().trainingName(null).date(null).duration(45).build(),
            TrainingDto.builder().trainingName("Zumba next workout").date(LocalDate.now().plusDays(3)).duration(null).build(),
            TrainingDto.builder().trainingName("Zumba next workout").date(LocalDate.now().plusDays(3)).duration(5).build(),
            TrainingDto.builder().trainingName("").date(LocalDate.now().plusDays(3)).duration(5).build(),
            TrainingDto.builder().trainingName("Zumba next workout").date(LocalDate.now()).duration(10).build()
    );

    @ParameterizedTest
    @FieldSource("notValidDto")
    void validateUserDtoFirstNameNull(TrainingDto trainingDto) {
        assertFalse(trainingDtoValidator.validate(trainingDto));
    }
}
