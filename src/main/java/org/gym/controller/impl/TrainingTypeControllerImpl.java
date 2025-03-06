package org.gym.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.controller.TrainingTypeController;
import org.gym.dto.response.trainingType.TrainingTypeResponse;
import org.gym.service.TrainingTypeService;
import org.gym.util.TransactionIdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainingtypes")
@Validated
public class TrainingTypeControllerImpl implements TrainingTypeController {

    private final TrainingTypeService trainingTypeService;
    private final TransactionIdGenerator transactionIdGenerator;
    
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TrainingTypeResponse> getAll() {
        String id = transactionIdGenerator.generate();
        List<TrainingTypeResponse> trainingTypeResponseList = trainingTypeService.findAll();
        LOGGER.info("response {}, transactionID {}", trainingTypeResponseList, id);
        return trainingTypeResponseList;
    }
}
