package org.gym.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.controller.TraineeController;
import org.gym.dto.*;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.facade.TraineeFacade;

import org.gym.mapper.TraineeMapper;
import org.gym.service.TraineeService;
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
//@Tag(name = "Trainees", description = "Operations related to managing trainees")
public class TraineeControllerImpl implements TraineeController {

    private final TraineeFacade traineeFacade;
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TransactionIdGenerator transactionIdGenerator;

    //    @SwaggerCreationInfo(
//            summary = "Create a trainee",
//            description = "Adds a new trainee",
//            schema = TraineeCreateResponse.class
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateResponse create(@RequestBody @Valid TraineeDto traineeDto){
        String id = transactionIdGenerator.generate();
        CreateResponse response = traineeFacade.create(traineeDto);
        LOGGER.info("request {}, response {}, id {}", traineeDto, response, id);
        return response;
    }

    @GetMapping("login/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> login(@PathVariable("username") String userName,
                                      @PathVariable("password") String password) {
        String id = transactionIdGenerator.generate();
        boolean response = traineeFacade.authenticate(userName, password);
        LOGGER.info("userName {}, response {}, id {}", userName, response, id);
        if(response) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/changelogin")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginRequest changeLoginRequest) {
        String id = transactionIdGenerator.generate();
        traineeFacade.changePassword(changeLoginRequest);
        LOGGER.info("userName {}, id {}", changeLoginRequest.getUserName(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeSelectResponse getTraineeProfile(@PathVariable("username") String userName) {
        String id = transactionIdGenerator.generate();
        TraineeSelectResponse response = traineeFacade.select(userName);
        LOGGER.debug("userName {}, response {}, id {}", userName, response, id);
        return response;
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeUpdateResponse update(@PathVariable("username") String userName,
            @RequestBody @Valid TraineeUpdateRequest traineeUpdateRequest) {
        String id = transactionIdGenerator.generate();
        TraineeUpdateResponse traineeUpdateResponse = traineeFacade.update(userName, traineeUpdateRequest);
        LOGGER.info("userName {}, id {}", userName, id);
        return traineeUpdateResponse;
    }

    @PatchMapping("/{username}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(
            @PathVariable("username") String userName,
            @RequestParam("isActive") Boolean isActive
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("userName: {}, isActive: {},  transaction id: {}", userName, isActive, id);

        traineeFacade.changeStatus(userName, isActive);
        LOGGER.info("User status updated for userName: {}, isActive: {}", userName, isActive);
    }

    @PutMapping("/updatetrainerslist/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerForListResponse> updateTrainersList(
            @PathVariable("username") String userName,
            @RequestBody List<String> trainersUserNamesList
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("userName: {}, trainersUsernames {}:  with transaction id: {}",
                userName, trainersUserNamesList, id);

        List<TrainerForListResponse> response = traineeFacade.updateTrainersList(userName, trainersUserNamesList);

        LOGGER.info("Response: {}, HTTP Status: {}", response, HttpStatus.OK);
        return response;
    }

    @DeleteMapping(value = "/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> delete(@PathVariable("username") String userName) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("delete: id {}, userName {}", id, userName);
        traineeFacade.delete(userName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unassignedtrainers/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerForListResponse> getUnassignedTrainers(
            @PathVariable("username") String userName
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("userName: {};  id: {}", userName, id);

        List<TrainerForListResponse> response = traineeFacade.getUnassignedTrainers(userName);

        LOGGER.info("Response: {}, HTTP Status: {}", response, HttpStatus.OK);
        return response;
    }
}