package org.gym;

import org.gym.dto.TraineeDto;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.dto.request.trainee.TraineeUpdateRequest;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.request.user.UserUpdateRequest;
import org.gym.dto.response.CreateResponse;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.gym.config.Config.*;

public class DataStorage {

    public final User user;
    public final String traineeUserName;
    public final Trainee trainee1;
    public final TraineeDto traineeDto1;
    public final TraineeDto traineeDtoNotValid;
    public final TraineeDto traineeDto;
    public final TraineeDto traineeDto2;

    public final Trainer trainer1;
    public final TrainerDto trainerDto1;
    public final Trainer trainer2;
    public final TrainerDto trainerDto2;
    public final String userNameForTrainerDto1;
    public final TrainingType trainerTrainingType;
    public final String trainerTrainingTypeName = "Zumba";

    public final String passwordForUser = "12345";
    public final String userNameNotFound = "bbbbbbb";

    public final ChangeLoginRequest changeLoginRequest;
    public final TrainerUpdateRequest trainerUpdateRequest;
    public final UserUpdateRequest userForTrainerUpdateRequest;

    public final TraineeUpdateRequest traineeUpdateRequest;
    public final UserUpdateRequest userForTraineeUpdateRequest;

    public final CreateResponse traineeCreateResponse;
    public final CreateResponse trainerCreateResponse;

    public final String exceptionMessageNotFound = String.format(ENTITY_NOT_FOUND_EXCEPTION, userNameNotFound);
    public final String exceptionMessageAccessDenied;

    public final List<String> expectedTrainingTypeNamesList = new ArrayList<>();
    public final List<TrainingType> trainingTypeList = new ArrayList<>();

    {
        String traineeAddress = "Vinnitsya, Soborna str. 35, ap. 26";
        String traineeAddress2 = "Kyiv, Khreschatik str. 35, ap. 26";
        user = new User(null, "Ivan", "Ivanenko", "Ivan.Ivanenko", passwordForUser, null);

        changeLoginRequest = ChangeLoginRequest.builder()
                .userName(user.getUserName())
                .oldPassword(user.getPassword())
                .newPassword("123456")
                .build();

        exceptionMessageAccessDenied = String.format(ACCESS_DENIED_EXCEPTION, changeLoginRequest.getUserName());

        userForTraineeUpdateRequest = UserUpdateRequest.builder()
                .firstName("Ivan")
                .lastName("Ivanenko")
                .isActive(true)
                .build();

        traineeUpdateRequest = TraineeUpdateRequest.builder()
                .user(userForTraineeUpdateRequest)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address(traineeAddress)
                .build();

        traineeUserName = "Ivan.Ivanenko";

        traineeDto1 = TraineeDto.builder()
                .user(UserDto.builder()
                        .userName(traineeUserName)
                        .firstName("Ivan")
                        .lastName("Ivanenko")
                        .build())
                .address(traineeAddress)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        UserDto userDto = new UserDto("Ivan", "Ivanenko", "Ivan.Ivanenko", null);
        UserDto userDto2 = new UserDto("Petro", "Petrenko", "Petro.Petrenko", null);

        traineeCreateResponse = CreateResponse.builder()
                .userName(userDto.getUserName())
                .password(passwordForUser)
                .build();

        traineeDto = TraineeDto.builder()
                .user(userDto)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address(traineeAddress)
                .build();

        traineeDto2 = TraineeDto.builder()
                .user(userDto2)
                .dateOfBirth(LocalDate.of(1985, 1, 23))
                .address(traineeAddress2)
                .build();

        UserDto userDtoNotValid = new UserDto("Pa", "Pa", "Maria.Petrenko2", false);

        traineeDtoNotValid = TraineeDto.builder()
                .user(userDtoNotValid)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address(traineeAddress2)
                .build();


        trainee1 = Trainee.builder()
                .user(user)
                .address(traineeAddress)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        // trainers section!

        trainer1 = Trainer.builder()
                .trainees(List.of(trainee1))
                .user(User.builder()
                        .firstName("Petro")
                        .lastName("Petrenko")
                        .userName("Petro.Petrenko")
                        .password(passwordForUser)
                        .isActive(null)
                        .build())
                .specialization(TrainingType.builder()
                        .trainingTypeName(trainerTrainingTypeName)
                        .build())
                .build();

        trainerCreateResponse = CreateResponse.builder()
                .userName(trainer1.getUser().getUserName())
                .password(passwordForUser)
                .build();

        trainerDto1 = TrainerDto.builder()
                .user(UserDto.builder()
                        .firstName("Petro")
                        .lastName("Petrenko")
                        .userName("Petro.Petrenko")
                        .isActive(true)
                        .build())
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName(trainerTrainingTypeName)
                        .build())
                .build();

        userNameForTrainerDto1 = trainerDto1.getUser().getUserName();
        trainerTrainingType = trainer1.getSpecialization();

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

        // TrainingType section

        expectedTrainingTypeNamesList.add("fitness");
        expectedTrainingTypeNamesList.add("yoga");
        expectedTrainingTypeNamesList.add("Zumba");
        expectedTrainingTypeNamesList.add("stretching");
        expectedTrainingTypeNamesList.add("resistance");

        trainingTypeList.add(new TrainingType(1L, "fitness"));
        trainingTypeList.add(new TrainingType(2L, "yoga"));
        trainingTypeList.add(new TrainingType(3L, "Zumba"));
        trainingTypeList.add(new TrainingType(4L, "stretching"));
        trainingTypeList.add(new TrainingType(5L, "resistance"));
    }
}
