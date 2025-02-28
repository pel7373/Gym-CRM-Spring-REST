package org.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeLoginRequest {
    @NotBlank
    private String userName;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
