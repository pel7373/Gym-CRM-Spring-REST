package org.gym;

import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.request.user.UserUpdateRequest;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.entity.User;

import java.time.LocalDate;
import java.util.List;

import static org.gym.config.Config.*;

public class DataStorage {

    public final String traineeUserName;
    public final Trainee trainee1;
    public final Trainer trainer1;
    public final TrainerDto trainerDto1;
    public final Trainer trainer2;
    public final TraineeDto traineeDto1;
    public final TrainerDto trainerDto2;
    public final TraineeDto traineeDtoNotValid;
    public final String passwordForUser = "12345";
    public final String userNameNotFound = "bbbbbbb";
    public final String userNameForTrainerDto = "Maria.Petrenko";

    public final TraineeDto traineeDto;
    public final TraineeDto traineeDto2;

    public final ChangeLoginRequest changeLoginRequest;
    public final TrainerUpdateRequest trainerUpdateRequest;
    public final UserUpdateRequest userForTrainerUpdateRequest;

    public final TraineeUpdateRequest traineeUpdateRequest;
    public final UserUpdateRequest userForTraineeUpdateRequest;

    public final String exceptionMessageNotFound = String.format(ENTITY_NOT_FOUND_EXCEPTION, userNameNotFound);
    public final String exceptionMessageAccessDenied;

    {
        changeLoginRequest = ChangeLoginRequest.builder()
                .userName("Ivan.Ivanenko")
                .oldPassword("12345")
                .newPassword("123456")
                .build();

        exceptionMessageAccessDenied = String.format(ACCESS_DENIED_EXCEPTION, changeLoginRequest.getUserName());

        userForTraineeUpdateRequest = UserUpdateRequest.builder()
                .firstName("Ivan")
                .lastName("Ivanenko")
                .isActive(true)
                .build();

        traineeUpdateRequest = TraineeUpdateRequest.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Vinnitsya, Soborna str.")
                .build();

        traineeUserName = "Ivan.Ivanenko";

//        traineeCreateRequest = TraineeCreateRequest.builder()
//                .user(UserCreateRequest.builder()
//                        .firstName("Ivan")
//                        .lastName("Ivanenko")
//                        .build())
//                .dateOfBirth(LocalDate.of(2000, 1, 1))
//                .address("Vinnitsya, Soborna str.")
//                .build();

        traineeDto1 = TraineeDto.builder()
                .user(UserDto.builder()
                        .userName(traineeUserName)
                        .firstName("Ivan")
                        .lastName("Ivanenko")
                        .build())
                .address("Vinnitsya, Soborna str.")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", "",true);
        UserDto userDto2 = new UserDto("Petro", "Ivanenko", "Petro.Ivanenko", "", true);

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

        UserDto userDtoNotValid = new UserDto("Pa", "Pa", "Maria.Petrenko2", "",false);

        traineeDtoNotValid = TraineeDto.builder()
                .user(userDtoNotValid)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
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

        userForTrainerUpdateRequest = UserUpdateRequest.builder()
                .firstName("Maria")
                .lastName("Petrenko")
                .isActive(true)
                .build();
        trainerUpdateRequest = TrainerUpdateRequest.builder()
                .user(userForTrainerUpdateRequest)
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();
    }
}
