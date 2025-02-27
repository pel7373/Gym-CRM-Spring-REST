package org.gym.controller;

import jakarta.validation.Valid;
import org.gym.controller.annotation.SwaggerCreationInfo;
import org.gym.controller.annotation.SwaggerDeletionInfo;
import org.gym.controller.annotation.SwaggerOperationInfo;
import org.gym.controller.annotation.responses.CreationResponse;
import org.gym.controller.annotation.responses.DeletionResponse;
import org.gym.controller.annotation.responses.OperationResponse;
import org.gym.dto.TraineeDto;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TraineeController {

    @SwaggerCreationInfo(
            summary = "Create the new trainee",
            description = "Create the new trainee",
            schema = CreateResponse.class
    )
    @CreationResponse
    CreateResponse create(@RequestBody @Valid TraineeDto traineeDto);


    @SwaggerOperationInfo(
            summary = "Get a trainee",
            description = "Get data of the trainee by its username",
            schema = TraineeSelectResponse.class
    )
    @OperationResponse
    TraineeSelectResponse getTraineeProfile(@PathVariable("username") String userName);

    @SwaggerOperationInfo(
            summary = "Update a trainee",
            description = "Updates details of an trainee by its username",
            schema = TraineeUpdateResponse.class
    )
    @OperationResponse
    TraineeUpdateResponse update(@PathVariable("username") String userName,
                                 @RequestBody @Valid TraineeUpdateRequest traineeUpdateRequest);


    List<TrainerForListResponse> updateTrainersList(
            @PathVariable("username") String userName,
            @RequestBody List<String> trainersUserNamesList
    );

    @SwaggerDeletionInfo(
            summary = "Delete the trainee",
            description = "Delete the trainee by its username"
    )
    @DeletionResponse
    ResponseEntity<Void> delete(@PathVariable("username") String userName);

    @SwaggerOperationInfo(
            summary = "Get trainee's unassigned trainers",
            description = "Get a list of trainers who are not assigned to the trainee",
            schema = TrainerForListResponse.class
    )
    @OperationResponse
    List<TrainerForListResponse> getUnassignedTrainers(
            @PathVariable("username") String userName
    );
}
