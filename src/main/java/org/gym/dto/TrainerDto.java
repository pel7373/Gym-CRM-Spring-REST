package org.gym.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerDto {

    @NotNull(message = "User cannot be null")
    private UserDto user;

    private TrainingTypeDto specialization;

    @Valid
    @JsonIgnore
    private List<TraineeDto> trainees;
}
