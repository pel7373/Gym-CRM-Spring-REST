package org.gym.dto.response.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.response.trainee.TraineeForListResponse;
import org.gym.dto.response.user.UserUpdateResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerUpdateResponse {
    private UserUpdateResponse user;

    private TrainingTypeDto specialization;
    private List<TraineeForListResponse> trainees = new ArrayList<>();
}
