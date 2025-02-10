package org.gym.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeProfileResponse {
    private UserProfileResponse user;
    private LocalDate dateOfBirth;
    private String address;
    private List<TrainerResponse> trainers;
}
