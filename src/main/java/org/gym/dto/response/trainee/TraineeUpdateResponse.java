package org.gym.dto.response.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.dto.response.user.UserUpdateResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeUpdateResponse {
    private UserUpdateResponse user;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;

    private List<TrainerForListResponse> trainers = new ArrayList<>();
}
