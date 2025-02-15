package org.gym.dto.response.trainer;

import lombok.*;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.response.user.UserForListResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerForListResponse {

    //private UserForListResponse user;
    private String userName;
    private String firstName;
    private String lastName;

    private TrainingTypeDto specialization;
}
