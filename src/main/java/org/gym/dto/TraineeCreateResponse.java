package org.gym.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeCreateResponse implements Serializable {
    private String userName;
    private String password;
}
