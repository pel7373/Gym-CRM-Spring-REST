package org.gym.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.controller.TrainingController;
import org.gym.dto.*;
import org.gym.dto.request.training.TraineeTrainingsCriteria;
import org.gym.dto.request.training.TrainerTrainingsCriteria;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;
import org.gym.facade.TrainingFacade;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainings")
@Validated
public class TrainingControllerImpl implements TrainingController {

    private final TrainingFacade trainingFacade;
    private final TransactionIdGenerator transactionIdGenerator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTraining(@RequestBody @Valid TrainingAddRequest request) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("request starts to processing: transaction id: {}", id);
        trainingFacade.create(request);
        LOGGER.info("HTTP Status: {}", HttpStatus.CREATED);
    }

    @GetMapping("/trainee")
    @ResponseStatus(HttpStatus.OK)
    public List<TraineeTrainingsListResponse> getTraineeTrainings(
            @ModelAttribute @Valid TraineeTrainingsDto traineeTrainingsDto
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("traineeTrainingsDto {}, transaction id: {}", traineeTrainingsDto, id);

        List<TraineeTrainingsListResponse> response = trainingFacade.getTraineeTrainings(
                traineeTrainingsDto
        );

        LOGGER.info("Response: {}, HTTP Status: {}", response, HttpStatus.OK);
        return response;
    }

    @GetMapping("/trainer")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerTrainingsListResponse> getTrainerTrainings(
            @ModelAttribute @Valid TrainerTrainingsDto trainerTrainingsDto
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("request {}, transaction id: {}", trainerTrainingsDto, id);

        List<TrainerTrainingsListResponse> response =
                trainingFacade.getTrainerTrainings(trainerTrainingsDto);

        LOGGER.info("Response: {}, HTTP Status: {}", response, HttpStatus.OK);
        return response;
    }
}
