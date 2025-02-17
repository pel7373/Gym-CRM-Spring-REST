package org.gym.controller;

import jakarta.validation.Valid;
import org.gym.dto.TraineeDto;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TraineeController {
    CreateResponse create(@RequestBody @Valid TraineeDto traineeDto);
    ResponseEntity<Void> login(@PathVariable("username") String userName,
                               @PathVariable("password") String password);
    ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginRequest changeLoginRequest);
    TraineeSelectResponse getTraineeProfile(@PathVariable("username") String userName);
    TraineeUpdateResponse update(@PathVariable("username") String userName,
                                 @RequestBody @Valid TraineeUpdateRequest traineeUpdateRequest);
    void changeStatus(@PathVariable("username") String userName,
                      @RequestParam("isActive") Boolean isActive);
    List<TrainerForListResponse> updateTrainersList(
            @PathVariable("username") String userName,
            @RequestBody List<String> trainersUserNamesList
    );

    ResponseEntity<Void> delete(@PathVariable("username") String userName);

    List<TrainerForListResponse> getUnassignedTrainers(
            @PathVariable("username") String userName
    );
}
