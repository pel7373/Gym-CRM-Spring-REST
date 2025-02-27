package org.gym.controller.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.controller.UserController;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.service.UserService;
import org.gym.util.TransactionIdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@Validated
@Tag(name = "Users", description = "Operations related to managing trainees")
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final TransactionIdGenerator transactionIdGenerator;

    @GetMapping("/login/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> login(@PathVariable("username") @NotBlank String userName,
                                      @PathVariable("password") @NotBlank String password) {
        String id = transactionIdGenerator.generate();
        boolean response = userService.authenticate(userName, password);
        LOGGER.info("userName {}, response {}, id {}", userName, response, id);
        if(response) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginRequest changeLoginRequest) {
        String id = transactionIdGenerator.generate();
        userService.changePassword(changeLoginRequest);
        LOGGER.info("userName {}, id {}", changeLoginRequest.getUserName(), id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}/status")
    @ResponseStatus(HttpStatus.OK)
    public boolean changeStatus(
            @PathVariable("username") @NotBlank String userName
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("userName: {}, transaction id: {}", userName, id);

        boolean status = userService.changeStatus(userName);
        LOGGER.info("userName {}, status {}", userName, status);
        return status;
    }
}
