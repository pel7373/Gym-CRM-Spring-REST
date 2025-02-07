package org.gym.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    @NotNull
    //@ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String firstName;

    @NotNull
    //@ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String lastName;
}
