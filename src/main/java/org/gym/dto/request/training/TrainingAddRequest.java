package org.gym.dto.request.training;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gym.dto.TrainingTypeDto;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingAddRequest {
    @NotBlank(message = "trainee's userName is required")
    private String traineeUserName;

    @NotBlank(message = "trainer's userName is required")
    private String trainerUserName;

    @Pattern(regexp = "^[A-Z0-9][\\sa-z0-9.:-=]+$", message = "Training name must comply with trainingName's rules")
    @NotBlank(message = "Training name is required and must comply with trainingName's rules")
    @Schema(example = "Zumba")
    private String trainingName;

    @NotNull(message = "TrainingType is required and must be one of the list: 'fitness, yoga, Zumba, stretching, resistance'")
    @Valid
    private TrainingTypeDto trainingType;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Training date should be in the present or future")
    @Schema(example = "2025-12-12")
    private LocalDate date;

    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Training's duration must be at least 15 min.")
    @Schema(example = "45")
    private Integer duration;
}
