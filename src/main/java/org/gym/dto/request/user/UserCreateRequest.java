package org.gym.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
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
}
