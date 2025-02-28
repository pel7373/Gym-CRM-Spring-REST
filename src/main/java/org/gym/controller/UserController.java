package org.gym.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.gym.controller.annotation.SwaggerOperationInfo;
import org.gym.controller.annotation.responses.OperationResponse;
import org.gym.dto.request.ChangeLoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserController {
    @SwaggerOperationInfo(
            summary = "Authenticate the user",
            description = "Authentication of the user by validating the login and the password"
    )
    @OperationResponse
    ResponseEntity<Void> login(@PathVariable("username") @NotBlank String userName,
                               @PathVariable("password") @NotBlank String password);

    @SwaggerOperationInfo(
            summary = "Change the user's password",
            description = "The user's password could be updated by providing the current and the new ones"
    )
    @OperationResponse
    ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginRequest changeLoginRequest);

    @SwaggerOperationInfo(
            summary = "Change the trainee's status",
            description = "The active status of the trainee could be changed by providing the username and the new status"
    )
    @OperationResponse
    boolean changeStatus(@PathVariable("username") @NotBlank String userName);
}
