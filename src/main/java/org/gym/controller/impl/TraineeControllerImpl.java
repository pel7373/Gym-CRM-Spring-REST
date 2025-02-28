package org.gym.controller.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.controller.TraineeController;
import org.gym.dto.*;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;

import org.gym.service.TraineeService;
import org.gym.util.TransactionIdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainees")
@Validated
@Tag(name = "Trainees", description = "Operations related to managing trainees")
public class TraineeControllerImpl implements TraineeController {

    private final TraineeService traineeService;
    private final TransactionIdGenerator transactionIdGenerator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateResponse create(@RequestBody @Valid TraineeDto traineeDto){
        String id = transactionIdGenerator.generate();
        CreateResponse response = traineeService.create(traineeDto);
        LOGGER.info("request {}, response {}, id {}", traineeDto, response, id);
        return response;
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeSelectResponse getTraineeProfile(@PathVariable("username") @NotBlank String userName) {
        String id = transactionIdGenerator.generate();
        TraineeSelectResponse response = traineeService.select(userName);
        LOGGER.debug("userName {}, response {}, id {}", userName, response, id);
        return response;
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeUpdateResponse update(@PathVariable("username") @NotBlank String userName,
            @RequestBody @Valid TraineeUpdateRequest traineeUpdateRequest) {
        String id = transactionIdGenerator.generate();
        TraineeUpdateResponse traineeUpdateResponse = traineeService.update(userName, traineeUpdateRequest);
        LOGGER.info("userName {}, id {}", userName, id);
        return traineeUpdateResponse;
    }

    @PutMapping("/{username}/trainers")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerForListResponse> updateTrainersList(
            @PathVariable("username") @NotBlank String userName,
            @RequestBody @NotEmpty List<String> trainersUserNamesList
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("userName: {}, trainersUsernames {}:  with transaction id: {}",
                userName, trainersUserNamesList, id);

        List<TrainerForListResponse> response = traineeService.updateTrainersList(userName, trainersUserNamesList);

        LOGGER.info("Response: {}, HTTP Status: {}", response, HttpStatus.OK);
        return response;
    }

    @DeleteMapping(value = "/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> delete(@PathVariable("username") @NotBlank String userName) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("delete: id {}, userName {}", id, userName);
        traineeService.delete(userName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/unassigned")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerForListResponse> getUnassignedTrainers(
            @PathVariable("username") @NotBlank String userName
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("userName: {};  id: {}", userName, id);

        List<TrainerForListResponse> response = traineeService.getUnassignedTrainersList(userName);

        LOGGER.info("Response: {}, HTTP Status: {}", response, HttpStatus.OK);
        return response;
    }
}