package org.gym.facade;

import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.TrainerDto;
import org.gym.entity.TrainingType;

public interface TrainerFacade {
    CreateResponse create(TrainerDto trainerDto);
    TrainerDto select(String userName, String password);
    TrainerDto update(String userName, String password, TrainerDto trainerDto);
    TrainerDto changeStatus(String userName, String password, Boolean isActive);
    boolean authenticate(String userName, String password);
    void changePassword(ChangeLoginRequest changeLoginRequest);
    TrainerDto changeSpecialization(String userName, String password, TrainingType trainingType);
}
