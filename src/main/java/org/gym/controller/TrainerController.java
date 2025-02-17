package org.gym.controller;

import jakarta.validation.Valid;
import org.gym.dto.TrainerDto;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface TrainerController {
    CreateResponse create(@RequestBody @Valid TrainerDto trainerDto);
    ResponseEntity<Void> login(@PathVariable("username") String userName,
                                      @PathVariable("password") String password);

    ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginRequest changeLoginRequest);

    TrainerSelectResponse getTrainerProfile(@PathVariable("username") String userName);

    TrainerUpdateResponse update(@PathVariable("username") String userName,
                                 @RequestBody @Valid TrainerUpdateRequest trainerUpdateRequest);
    void changeStatus(
            @PathVariable("username") String userName,
            @RequestParam("isActive") Boolean isActive
    );


}
