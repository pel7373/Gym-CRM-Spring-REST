package org.gym.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    private UserDto user;

    //@ToString.Exclude
    @EqualsAndHashCode.Exclude
    private LocalDate dateOfBirth;

    //@ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String address;

    @Valid
    private List<TrainerDto> trainers = new ArrayList<>();
}
