package org.gym.dto.request.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.gym.dto.request.user.UserUpdateRequest;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeUpdateRequest {
    @NotNull(message = "User is required")
    @Valid
    private UserUpdateRequest user;

    @Past(message = "Date of birth should be in the past")
    @ToString.Exclude
    private LocalDate dateOfBirth;

    @ToString.Exclude
    private String address;
}
