package org.gym.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TrainingTypeResponse;
import org.gym.entity.TrainingType;
import org.gym.facade.TraineeFacade;
import org.gym.facade.TrainingTypeFacade;
import org.gym.mapper.TraineeMapper;
import org.gym.mapper.TrainingTypeMapper;
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
    private final TrainingTypeMapper trainingTypeMapper;
    private final TransactionIdGenerator transactionIdGenerator;
    
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TrainingTypeResponse>> getAll() {
        String id = transactionIdGenerator.generate();
        List<TrainingType> trainingTypeList = trainingTypeFacade.findAll();

        List<TrainingTypeResponse> response = trainingTypeList.stream()
                .map(trainingTypeMapper::convertToTrainingTypeResponse)
                .toList();
        LOGGER.info("getAll: transactionID {}, response {}", id, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
