package org.gym.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.controller.TrainerController;
import org.gym.dto.*;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.service.TrainerService;
import org.gym.util.TransactionIdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainers")
@Validated
public class TrainerControllerImpl implements TrainerController {

    private final TrainerService trainerService;
    private final TransactionIdGenerator transactionIdGenerator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateResponse create(@RequestBody @Valid TrainerDto trainerDto) {
        String id = transactionIdGenerator.generate();
        CreateResponse response = trainerService.create(trainerDto);
        LOGGER.info("request {}, response {}, id {}", trainerDto, response, id);
        return response;
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TrainerSelectResponse getTrainerProfile(@PathVariable("username") String userName) {
        String id = transactionIdGenerator.generate();
        TrainerSelectResponse response = trainerService.select(userName);
        LOGGER.debug("userName {}, response {}, id {}", userName, response, id);
        return response;
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TrainerUpdateResponse update(@PathVariable("username") String userName,
                                        @RequestBody @Valid TrainerUpdateRequest trainerUpdateRequest) {
        String id = transactionIdGenerator.generate();
        TrainerUpdateResponse trainerUpdateResponse = trainerService.update(userName, trainerUpdateRequest);
        LOGGER.info("userName {}, id {}", userName, id);
        return trainerUpdateResponse;
    }
}
