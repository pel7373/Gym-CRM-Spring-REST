package org.gym.controller;

import org.gym.dto.response.trainingType.TrainingTypeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainingTypeController {
    ResponseEntity<List<TrainingTypeResponse>> getAll();
}
