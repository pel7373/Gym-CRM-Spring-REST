package org.gym.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingDto {

    @NotNull(message = "Trainee for training is required")
    @Valid
    private TraineeDto trainee;

    @NotNull(message = "Trainer for training is required")
    @Valid
    private TrainerDto trainer;

    @Pattern(regexp = "^[A-Z0-9][\\sa-z0-9.:-=]+$", message = "Training name must comply with trainingName's rules")
    @NotNull(message = "Training name is required")
    private String trainingName;

    @NotNull(message = "TrainingType is required")
    private TrainingTypeDto trainingType;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Training's duration must be at least 15 min.")
    private Integer duration;
}
