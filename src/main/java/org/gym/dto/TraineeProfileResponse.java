package org.gym.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeProfileResponse {
    private UserGetProfileResponse user;
    private LocalDate dateOfBirth;
    private String address;
    private List<TrainerResponse> trainers;
}
