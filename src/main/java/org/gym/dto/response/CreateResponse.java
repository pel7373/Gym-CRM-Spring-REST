package org.gym.dto.response;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateResponse implements Serializable {
    private String userName;
    private String password;
}
