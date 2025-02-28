package org.gym.dto.response.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.gym.dto.TrainingTypeDto;


import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TraineeTrainingsListResponse {

    private String trainingName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private TrainingTypeDto trainingType;

    private Integer duration;

    private String trainerUserName;
}
