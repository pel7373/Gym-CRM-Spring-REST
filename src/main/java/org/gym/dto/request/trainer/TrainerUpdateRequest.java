package org.gym.dto.request.trainer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.request.user.UserUpdateRequest;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TrainerUpdateRequest {

    @NotNull(message = "User is required")
    @Valid
    private UserUpdateRequest user;

    @NotNull(message = "Specialization is required")
    private TrainingTypeDto specialization;
}
