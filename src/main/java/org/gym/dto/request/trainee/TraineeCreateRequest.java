package org.gym.dto.request.trainee;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.gym.dto.request.user.UserCreateRequest;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class TraineeCreateRequest implements Serializable {
    @NotNull
    @Valid
    private UserCreateRequest user;

    private LocalDate dateOfBirth;
    private String address;
}
