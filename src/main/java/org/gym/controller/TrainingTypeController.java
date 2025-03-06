package org.gym.controller;

import org.gym.dto.response.trainingType.TrainingTypeResponse;

import java.util.List;

public interface TrainingTypeController {
    List<TrainingTypeResponse> getAll();
}
