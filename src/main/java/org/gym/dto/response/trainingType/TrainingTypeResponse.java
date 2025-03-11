package org.gym.dto.response.trainingtype;

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
