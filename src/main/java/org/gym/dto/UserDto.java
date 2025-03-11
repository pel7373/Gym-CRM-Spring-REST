package org.gym.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotBlank(message = "First name is required")
    @Size(min = 2, message = "User's first name must be at least 2 letters long")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "User's first name must at least 2 letters long and consist of letters only (the first one is capital)")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, message = "User's last name must be at least 2 letters long")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "User's last name must at least 2 letters long and consist of letters only (the first one is capital)")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String lastName;

    private String userName;
    private Boolean isActive;
}
