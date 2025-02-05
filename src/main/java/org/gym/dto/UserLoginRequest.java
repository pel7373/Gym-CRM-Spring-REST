package org.gym.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class UserLoginRequest {
    @NotNull(message = "User shouldn't be null")
    String userName;

    @NotNull(message = "Password shouldn't be null")
    String password;

    @Override
    public String toString() {
        return "UserLoginRequest{}";
    }
}
