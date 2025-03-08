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
@Tag(name = "Users", description = "Operations related to managing users")
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final TransactionIdGenerator transactionIdGenerator;

    @GetMapping("/login/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> login(@PathVariable("username") @NotBlank String userName,
                                      @PathVariable("password") @NotBlank String password) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("GET /api/v1/login/{}/<password> called  with transaction id: {}", userName, id);
        boolean response = userService.authenticate(userName, password);
        HttpStatus status = response ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        LOGGER.info("result of login for userName {}: {}, status {}", userName, response, status);
        return new ResponseEntity<>(status);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginRequest changeLoginRequest) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("PUT /api/v1/password called changeLoginRequest for userName {} with transaction id: {}", changeLoginRequest.getUserName(), id);
        userService.changePassword(changeLoginRequest);
        LOGGER.info("password updated for userName {}", changeLoginRequest.getUserName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}/status")
    @ResponseStatus(HttpStatus.OK)
    public boolean changeStatus(
            @PathVariable("username") @NotBlank String userName
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("PATCH /api/v1/{}/status, transaction id: {}", userName, id);

        boolean status = userService.changeStatus(userName);
        LOGGER.info("status was changed for userName {}: {}", userName, status);
        return status;
    }
}
