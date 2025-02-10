package org.gym.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForListResponse {
    private String firstName;
    private String lastName;
    private String userName;
}
