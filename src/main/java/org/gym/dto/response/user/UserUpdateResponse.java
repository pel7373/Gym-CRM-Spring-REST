package org.gym.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateResponse {

    private String userName;
    private String firstName;
    private String lastName;
    private Boolean isActive;

}
