package org.gym.dto;

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
    private String traineeUserName;
    private LocalDate fromDate;
    private java.time.LocalDate toDate;
    private String trainerUserName;
    private String trainingType;
}
