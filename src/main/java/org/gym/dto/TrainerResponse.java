package org.gym.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerResponse {

    private UserForListResponse user;
    private TrainingTypeDto specialization;
}
