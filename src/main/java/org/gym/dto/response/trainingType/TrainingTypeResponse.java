package org.gym.dto.response.trainingType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingTypeResponse {
    private String trainingTypeName;
    private Long id;
}
