package org.gym.dto.response.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserSelectResponse {
    private String firstName;
    private String lastName;
    private Boolean isActive;
}
