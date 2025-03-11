package org.gym.service;

import org.gym.dto.response.trainingtype.TrainingTypeResponse;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeResponse> findAll();
}
