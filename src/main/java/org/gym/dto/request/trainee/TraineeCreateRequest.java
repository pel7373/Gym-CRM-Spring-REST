package org.gym.dto.request.trainee;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.gym.dto.request.user.UserCreateRequest;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeCreateRequest implements Serializable {
    @NotNull
    private UserCreateRequest user;
    private LocalDate dateOfBirth;
    private String address;
}
