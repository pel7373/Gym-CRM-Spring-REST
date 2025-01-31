package org.gym.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeDto {

    @NotNull(message = "UserDto is required")
    @NotBlank
    private UserDto user;

    @ToString.Exclude
    private LocalDate dateOfBirth;

    @ToString.Exclude
    private String address;
}
