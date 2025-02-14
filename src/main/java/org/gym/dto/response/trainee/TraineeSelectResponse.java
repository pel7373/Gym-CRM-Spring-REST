package org.gym.dto.response.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.dto.response.user.UserSelectResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeSelectResponse {

    private UserSelectResponse user;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;

    private List<TrainerForListResponse> trainers = new ArrayList<>();
}
