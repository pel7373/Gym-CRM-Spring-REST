package org.gym.dto.response.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForListResponse {
    private String userName;
    private String firstName;
    private String lastName;
}
