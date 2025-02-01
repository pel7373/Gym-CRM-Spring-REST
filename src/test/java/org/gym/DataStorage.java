package org.gym;

import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.entity.User;

import java.time.LocalDate;
import java.util.List;

public class DataStorage {

    public final String traineeUserName;
    public final Trainee trainee1;
    public final Trainer trainer1;
    public final TrainerDto trainerDto1;
    public final Trainer trainer2;
    public final TraineeDto traineeDto1;
    public final TrainerDto trainerDto2;

    public final TraineeDto traineeDto;
    public final TraineeDto traineeDto2;

    {
        traineeUserName = "Ivan.Ivanenko";
        traineeDto1 = TraineeDto.builder()
                .user(UserDto.builder()
                        .userName(traineeUserName)
                        .firstName("Ivan")
                        .lastName("Ivanenko")
                        .build())
                .address("Vinnitsya, Soborna str.")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();


        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        UserDto userDto2 = new UserDto("Petro", "Ivanenko", "Petro.Ivanenko", true);

        traineeDto = TraineeDto.builder()
                .user(userDto)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        traineeDto2 = TraineeDto.builder()
                .user(userDto2)
                .dateOfBirth(LocalDate.of(1985, 1, 23))
                .address("Kyiv, Soborna str. 35, ap. 26")
                .build();


        trainee1 = Trainee.builder()
                .user(User.builder()
                        .userName(traineeUserName)
                        .firstName("Ivan")
                        .lastName("Ivanenko")
                        .password("password")
                        .isActive(true)
                        .build())
                .address("Vinnitsya, Soborna str.")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        trainer1 = Trainer.builder()
                .trainees(List.of(trainee1))
                .user(User.builder()
                        .firstName("Petro")
                        .lastName("Petrenko")
                        .userName("Petro.Petrenko")
                        .password("password")
                        .isActive(true)
                        .build())
                .specialization(TrainingType.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        trainerDto1 = TrainerDto.builder()
                .user(UserDto.builder()
                        .firstName("Petro")
                        .firstName("Petrenko")
                        .userName("Petro.Petrenko")
                        .isActive(true)
                        .build())
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        trainer2 = Trainer.builder()
                .trainees(List.of())
                .user(User.builder()
                        .firstName("PetroPetro")
                        .lastName("Petrenko")
                        .userName("PetroPetro.Petrenko")
                        .password("password")
                        .isActive(true)
                        .build())
                .specialization(TrainingType.builder()
                        .trainingTypeName("yoga")
                        .build())
                .build();


        trainerDto2 = TrainerDto.builder()
                .user(UserDto.builder()
                        .firstName("PetroPetro")
                        .lastName("Petrenko")
                        .userName("PetroPetro.Petrenko")
                        .isActive(true)
                        .build())
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("yoga")
                        .build())
                .build();
    }
}
