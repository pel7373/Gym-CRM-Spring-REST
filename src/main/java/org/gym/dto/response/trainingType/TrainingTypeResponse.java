package org.gym.dto.response.trainingType;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TrainingTypeResponse {
    private String trainingTypeName;
    private Long id;
}
