package org.gym.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.entity.TrainingType;
import org.gym.facade.TraineeFacade;
import org.gym.facade.TrainingTypeFacade;
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
@RequestMapping(value = "/api/v1/trainingtypes")
@Validated
public class TrainingTypeController {
    private final TrainingTypeFacade trainingTypeFacade;
    private final TraineeMapper traineeMapper;
    private final TransactionIdGenerator transactionIdGenerator;
    
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TrainingType>> getAll() {
        String id = transactionIdGenerator.generate();
        List<TrainingType> response = trainingTypeFacade.findAll();
        LOGGER.info("getAll: transactionID {}, response {}", id, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
