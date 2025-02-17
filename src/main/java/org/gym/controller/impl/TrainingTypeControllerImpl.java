package org.gym.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.controller.TrainingTypeController;
import org.gym.dto.response.trainingType.TrainingTypeResponse;
import org.gym.entity.TrainingType;
import org.gym.facade.TrainingTypeFacade;
import org.gym.mapper.TrainingTypeMapper;
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
public class TrainingTypeControllerImpl implements TrainingTypeController {
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
