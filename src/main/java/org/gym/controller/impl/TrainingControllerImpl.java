package org.gym.controller.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.controller.TrainingController;
import org.gym.dto.*;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;
import org.gym.service.TrainingService;
import org.gym.util.TransactionIdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainings")
@Validated
@Tag(name = "Trainings", description = "Operations related to managing trainings")
public class TrainingControllerImpl implements TrainingController {

    private final TrainingService trainingService;
    private final TransactionIdGenerator transactionIdGenerator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTraining(@RequestBody @Valid TrainingAddRequest request) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("POST /api/v1/trainings, request {} with transaction id {}", request, id);
        trainingService.create(request);
        LOGGER.info("training created, HTTP status: {}", HttpStatus.CREATED);
    }

    @GetMapping("/trainee")
    @ResponseStatus(HttpStatus.OK)
    public List<TraineeTrainingsListResponse> getTraineeTrainings(
            @ModelAttribute @Valid TraineeTrainingsDto traineeTrainingsDto
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("GET /api/v1/trainings/trainee called with request {},  transaction id: {}", traineeTrainingsDto, id);

        List<TraineeTrainingsListResponse> response = trainingService.getTraineeTrainingsListCriteria(
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
        LOGGER.info("GET /api/v1/trainings/trainer called with request {},  transaction id: {}", trainerTrainingsDto, id);

        List<TrainerTrainingsListResponse> response =
                trainingService.getTrainerTrainingsListCriteria(trainerTrainingsDto);

        LOGGER.info("Response: {}, HTTP Status: {}", response, HttpStatus.OK);
        return response;
    }
}
