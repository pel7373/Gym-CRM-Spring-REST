package org.gym.dto.response.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserForListResponse {
    private String userName;
    private String firstName;
    private String lastName;
}
