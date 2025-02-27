package org.gym.controller;

import jakarta.validation.Valid;
import org.gym.controller.annotation.SwaggerCreationInfo;
import org.gym.controller.annotation.SwaggerOperationInfo;
import org.gym.controller.annotation.responses.CreationResponse;
import org.gym.controller.annotation.responses.OperationResponse;
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

    @SwaggerCreationInfo(
            summary = "Create the new trainer",
            description = "Create the new trainer",
            schema = CreateResponse.class
    )
    @CreationResponse
    CreateResponse create(@RequestBody @Valid TrainerDto trainerDto);

    @SwaggerOperationInfo(
            summary = "Get a trainer",
            description = "Get data of the trainee by its username",
            schema = TrainerSelectResponse.class
    )
    @OperationResponse
    TrainerSelectResponse getTrainerProfile(@PathVariable("username") String userName);

    @SwaggerOperationInfo(
            summary = "Update a trainer",
            description = "Updates details of an trainer by its username",
            schema = TrainerUpdateResponse.class
    )
    @OperationResponse
    TrainerUpdateResponse update(@PathVariable("username") String userName,
                                 @RequestBody @Valid TrainerUpdateRequest trainerUpdateRequest);
}
