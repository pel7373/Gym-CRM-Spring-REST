package org.gym.dto.response.trainer;

import lombok.*;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.response.user.UserSelectResponse;
import org.gym.dto.response.trainee.TraineeForListResponse;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TrainerSelectResponse {
    private UserSelectResponse user;
    private TrainingTypeDto specialization;
    private List<TraineeForListResponse> trainees = new ArrayList<>();
}
