package org.gym.dto.response.trainee;

import lombok.*;
import org.gym.dto.response.user.UserForListResponse;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TraineeForListResponse {
    private UserForListResponse user;
}
