package org.gym.dto.response.trainer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.response.trainee.TraineeForListResponse;
import org.gym.dto.response.user.UserUpdateResponse;

import java.time.LocalDate;
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
