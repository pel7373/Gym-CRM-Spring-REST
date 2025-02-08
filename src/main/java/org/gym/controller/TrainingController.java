package org.gym.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.*;
import org.gym.facade.TraineeFacade;
import org.gym.facade.TrainingFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainings")
@Validated
public class TrainingController {
    private final TrainingFacade trainingFacade;
    private final TransactionIdGenerator transactionIdGenerator;

    @GetMapping("/trainer")
    public List<TrainerTrainingListResponse> getTrainerTrainings(
            //@PathVariable("username") String trainerUserName,
            @RequestBody @Valid TrainerTrainingsDto request
    ) {
        String id = transactionIdGenerator.generate();
        LOGGER.info("request: {}", request);
        List<TrainingDto> trainerTrainings = trainingFacade.getTrainerTrainings(request);
        return null;
    }
}
