package org.gym.dto.request.trainer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.request.user.UserCreateRequest;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class TrainerCreateRequest {
    @NotNull
    @Valid
    private UserCreateRequest user;

    @NotNull
    private TrainingTypeDto specialization;
}
