package org.gym.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateResponse {
    private String userName;
    private String password;
}
