package org.gym.dto.request.trainer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.request.user.UserCreateRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerCreateRequest {
    @NotNull
    private UserCreateRequest user;

    @NotNull
    private TrainingTypeDto specialization;
}
