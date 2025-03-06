package org.gym.service;

import org.gym.dto.response.trainingType.TrainingTypeResponse;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeResponse> findAll();
}
