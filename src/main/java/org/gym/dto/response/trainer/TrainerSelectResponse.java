package org.gym.dto.response.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.response.user.UserSelectResponse;
import org.gym.dto.response.trainee.TraineeForListResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerSelectResponse {
    private UserSelectResponse user;
    private TrainingTypeDto specialization;
    private List<TraineeForListResponse> trainees = new ArrayList<>();
}
