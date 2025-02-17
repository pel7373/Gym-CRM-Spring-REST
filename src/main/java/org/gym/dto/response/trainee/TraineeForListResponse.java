package org.gym.dto.response.trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gym.dto.response.user.UserForListResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeForListResponse {
    private UserForListResponse user;
}
