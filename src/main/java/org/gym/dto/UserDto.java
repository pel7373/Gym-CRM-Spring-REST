package org.gym.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotBlank(message = "First name is required")
    @Size(min = 4, message = "User's first name must be at least 4 letters long")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "User's first name must consist of letters only (the first one is capital)")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 4, message = "User's last name must be at least 4 letters long")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "User's last name must consist of letters only (the first one is capital)")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String lastName;

    private String userName;
    private String password;
    private Boolean isActive;
}
