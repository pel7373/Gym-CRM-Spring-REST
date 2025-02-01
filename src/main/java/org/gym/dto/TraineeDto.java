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
    @EqualsAndHashCode.Exclude
    private LocalDate dateOfBirth;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String address;
}
