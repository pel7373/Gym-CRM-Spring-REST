package org.gym;

import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.entity.User;

import java.time.LocalDate;
import java.util.List;

public class DataStorage2 {

    public final User user;
    public final Trainee trainee1;
    public final Trainer trainer1;
    public final Trainer trainer2;

    {
        user = new User(null, "Ivan", "Ivanenko", "Ivan.Ivanenko", DataStorage.passwordForUser, null);

        trainee1 = Trainee.builder()
                .user(user)
                .address(DataStorage.traineeAddress)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        trainer1 = Trainer.builder()
                .trainees(List.of(trainee1))
                .user(User.builder()
                        .firstName("Petro")
                        .lastName("Petrenko")
                        .userName("Petro.Petrenko")
                        .password(DataStorage.passwordForUser)
                        .isActive(null)
                        .build())
                .specialization(TrainingType.builder()
                        .trainingTypeName(DataStorage.trainerTrainingTypeName)
                        .build())
                .build();

        trainer2 = Trainer.builder()
                .trainees(List.of())
                .user(User.builder()
                        .firstName("Sergiy")
                        .lastName("Sidorenko")
                        .userName("Sergiy.Sidorenko")
                        .password("password")
                        .isActive(true)
                        .build())
                .specialization(TrainingType.builder()
                        .trainingTypeName("yoga")
                        .build())
                .build();
    }
}
