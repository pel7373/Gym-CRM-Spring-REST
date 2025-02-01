package org.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotEmpty(message = "First name is required")
    @NotBlank
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String firstName;

    @NotNull(message = "Last name is required")
    @NotBlank
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String lastName;

    private String userName;

    @NotNull(message = "isActive is required")
    @NotBlank
    private Boolean isActive;
}
