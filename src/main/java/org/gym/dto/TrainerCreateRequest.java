package org.gym.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
