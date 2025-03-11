package org.gym.controller;

import org.gym.controller.annotation.SwaggerOperationInfo;
import org.gym.controller.annotation.responses.GetAllResponse;
import org.gym.dto.response.trainingtype.TrainingTypeResponse;

import java.util.List;

public interface TrainingTypeController {

    @SwaggerOperationInfo(
            summary = "Get all training types",
            description = "Retrieves all details of training types",
            schema = TrainingTypeResponse.class
    )
    @GetAllResponse
    List<TrainingTypeResponse> getAll();
}
