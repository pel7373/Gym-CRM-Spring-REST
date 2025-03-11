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
public class TrainerTrainingsDto {
    @NotBlank(message = "trainer's userName is required")
    String trainerUserName;
    LocalDate fromDate;
    LocalDate toDate;
    String traineeUserName;
}
