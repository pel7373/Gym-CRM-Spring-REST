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
public class TrainerTrainingsDto {
    String trainerUserName;
    LocalDate fromDate;
    LocalDate toDate;
    String traineeUserName;
}
