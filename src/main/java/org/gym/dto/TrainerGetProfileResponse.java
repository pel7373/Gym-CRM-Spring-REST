package org.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerGetProfileResponse {
    private UserGetProfileResponse user;
    private TrainingTypeDto specialization;
    private List<TraineeForListResponse> trainees;
}
