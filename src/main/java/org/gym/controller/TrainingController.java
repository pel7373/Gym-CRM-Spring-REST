package org.gym.controller;

import jakarta.validation.Valid;
import org.gym.controller.annotation.SwaggerCreationInfo;
import org.gym.controller.annotation.SwaggerOperationInfo;
import org.gym.controller.annotation.responses.CreationResponse;
import org.gym.controller.annotation.responses.OperationResponse;
import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.dto.TrainingDto;
import org.gym.dto.request.training.TrainingAddRequest;
import org.gym.dto.response.training.TraineeTrainingsListResponse;
import org.gym.dto.response.training.TrainerTrainingsListResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TrainingController {

    @SwaggerCreationInfo(
            summary = "Add the new training",
            description = "Add the new training",
            schema = TrainingDto.class
    )
    @CreationResponse
    void addTraining(@RequestBody @Valid TrainingAddRequest request);

    @SwaggerOperationInfo(
            summary = "Get the trainee's trainings",
            description = "Get trainee's trainings by the username. Optional parameters: 1) date from...; 2) date to...; 3) the trainer's name; 4) training type.",
            schema = TraineeTrainingsListResponse.class
    )
    @OperationResponse
    List<TraineeTrainingsListResponse> getTraineeTrainings(
            @ModelAttribute @Valid TraineeTrainingsDto traineeTrainingsDto
    );

    @SwaggerOperationInfo(
            summary = "Get the trainer's trainings",
            description = "Get trainer's trainings by the username. Optional parameters: 1) date from...; 2) date to...; 3) the trainee's name.",
            schema = TrainerTrainingsListResponse.class
    )
    @OperationResponse
    List<TrainerTrainingsListResponse> getTrainerTrainings(
            @ModelAttribute @Valid TrainerTrainingsDto trainerTrainingsDto
    );

}
