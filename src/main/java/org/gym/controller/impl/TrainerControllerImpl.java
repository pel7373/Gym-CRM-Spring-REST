package org.gym.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.controller.TrainerController;
import org.gym.dto.*;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.facade.TrainerFacade;
import org.gym.mapper.TrainerMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainers")
@Validated
public class TrainerControllerImpl implements TrainerController {

    private final TrainerFacade trainerFacade;
    private final TransactionIdGenerator transactionIdGenerator;

    //    @SwaggerCreationInfo(
//            summary = "Create a trainee",
//            description = "Adds a new trainee",
//            schema = TraineeCreateResponse.class
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateResponse create(@RequestBody @Valid TrainerDto trainerDto) {
        String id = transactionIdGenerator.generate();
        CreateResponse response = trainerFacade.create(trainerDto);
        LOGGER.info("request {}, response {}, id {}", trainerDto, response, id);
        return response;
    }

    @GetMapping("login/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> login(@PathVariable("username") String userName,
                                      @PathVariable("password") String password) {
        String id = transactionIdGenerator.generate();
        boolean response = trainerFacade.authenticate(userName, password);
        LOGGER.info("userName {}, response {}, id {}", userName, response, id);
        if(response) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/changelogin")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginRequest changeLoginRequest) {
        String id = transactionIdGenerator.generate();
        trainerFacade.changePassword(changeLoginRequest);
        LOGGER.info("userName {}, id {}", changeLoginRequest.getUserName(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TrainerSelectResponse getTrainerProfile(@PathVariable("username") String userName) {
        String id = transactionIdGenerator.generate();
        TrainerSelectResponse response = trainerFacade.select(userName);
        LOGGER.debug("userName {}, response {}, id {}", userName, response, id);
        return response;
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TrainerUpdateResponse update(@PathVariable("username") String userName,
                                        @RequestBody @Valid TrainerUpdateRequest trainerUpdateRequest) {
        String id = transactionIdGenerator.generate();
        TrainerUpdateResponse trainerUpdateResponse = trainerFacade.update(userName, trainerUpdateRequest);
        LOGGER.info("userName {}, id {}", userName, id);
        return trainerUpdateResponse;
    }

    @PatchMapping("/{username}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(
            @PathVariable("username") String userName,
            @RequestParam("isActive") Boolean isActive
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("userName: {}, isActive: {},  transaction id: {}", userName, isActive, id);

        trainerFacade.changeStatus(userName, isActive);
        LOGGER.info("User status updated for userName: {}, isActive: {}", userName, isActive);
    }
}
