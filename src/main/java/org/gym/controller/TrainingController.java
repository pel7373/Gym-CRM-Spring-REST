package org.gym.controller;

import jakarta.validation.Valid;
import org.gym.dto.request.training.TrainingAddRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface TrainingController {
    void addTraining(@RequestBody @Valid TrainingAddRequest request);
}
