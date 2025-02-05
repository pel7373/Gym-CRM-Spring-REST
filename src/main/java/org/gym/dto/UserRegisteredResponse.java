package org.gym.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class UserRegisteredResponse {
    String userName;

    String password;

    @Override
    public String toString() {
        return "UserRegisteredResponse{}";
    }
}
