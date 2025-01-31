package org.gym.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingDto {

    private TraineeDto trainee;

    private TrainerDto trainer;

    @NotNull(message = "Training name is required")
    private String trainingName;

    private TrainingTypeDto trainingType;

    @NotNull(message = "Date is required")
    @NotBlank
    private LocalDate date;

    @NotNull(message = "Duration is required")
    @NotBlank
    private Integer duration;
}
