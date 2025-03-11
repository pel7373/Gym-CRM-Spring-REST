package org.gym.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeTrainingsDto {
    @NotBlank(message = "trainee's userName is required")
    private String traineeUserName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String trainerUserName;
    private String trainingType;
}
