package org.gym.dto.response.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

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

    private String trainingType;

    private Integer duration;

    private String trainerUserName;
}
