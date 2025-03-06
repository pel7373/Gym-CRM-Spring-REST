package org.gym.dto.response.trainingType;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TrainingTypeResponse {
    private String trainingTypeName;
    private Long id;
}
