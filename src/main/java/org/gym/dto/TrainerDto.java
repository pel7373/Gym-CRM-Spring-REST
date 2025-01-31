package org.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerDto {

    @NotNull(message = "User cannot be null")
    @NotBlank
    private UserDto user;

    private TrainingTypeDto specialization;
}
