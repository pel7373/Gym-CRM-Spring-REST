package org.gym.dto.response.trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeForListResponse {
    private String userName;
    private String firstName;
    private String lastName;
    //private UserForListResponse user;
}
