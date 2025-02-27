package org.gym.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerDto {

    @NotNull(message = "User cannot be null")
    @Valid
    private UserDto user;

    @NotNull(message = "Trainer's specialization is required")
    private TrainingTypeDto specialization;

    @Valid
    @JsonIgnore
    private List<TraineeDto> trainees = new ArrayList<>();
}
