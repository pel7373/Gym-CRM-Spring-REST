package org.gym.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    @NotBlank
    //@ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String firstName;

    @NotBlank
    //@ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String lastName;
}
