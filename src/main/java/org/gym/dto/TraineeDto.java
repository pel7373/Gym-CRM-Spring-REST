package org.gym.dto;

import jakarta.validation.Valid;
import lombok.*;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeDto {
    @NotNull(message = "UserDto is required")
    @Valid
    private UserDto user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private LocalDate dateOfBirth;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String address;

    @Valid
    private List<TrainerDto> trainers = new ArrayList<>();
}
