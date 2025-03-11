package org.gym.dto.response.trainer;

import lombok.*;
import org.gym.dto.response.trainee.TraineeForListResponse;
import org.gym.dto.response.user.UserUpdateResponse;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TrainerUpdateResponse {
    private UserUpdateResponse user;

    private String specialization;
    private List<TraineeForListResponse> trainees = new ArrayList<>();
}
