package org.gym.mapper;

import org.gym.DataStorage;
import org.gym.config.TestConfig;
import org.gym.dto.TraineeDto;
import org.gym.dto.UserDto;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainee.TraineeSelectResponse;
import org.gym.dto.response.trainee.TraineeUpdateResponse;
import org.gym.entity.Trainee;
import org.gym.entity.Trainer;
import org.gym.entity.TrainingType;
import org.gym.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebAppConfiguration
class TraineeMapperTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TraineeMapper traineeMapper;

    @Test
    void convertToDto() {
        User user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", "", true);

        Trainee trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        TraineeDto createdTraineeDto = traineeMapper.convertToDto(trainee);

        assertNotNull(createdTraineeDto);
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainee.getUser().getFirstName(), createdTraineeDto.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainee.getUser().getLastName(), createdTraineeDto.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainee.getUser().getUserName(), createdTraineeDto.getUser().getUserName(), "check userName"),
                () -> assertEquals(trainee.getUser().getIsActive(), createdTraineeDto.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainee.getDateOfBirth(), createdTraineeDto.getDateOfBirth(), "check date of birth"),
                () -> assertEquals(trainee.getAddress(), createdTraineeDto.getAddress(), "check address")
        );
    }

    @Test
    void convertToDtoWithNullTrainee() {
        TraineeDto traineeDto = traineeMapper.convertToDto(null);
        assertNull(traineeDto, "ConvertToDto: expect null when input is null");
    }

    @Test
    void convertToEntity() {
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);

        TraineeDto traineeDto = TraineeDto.builder()
                .user(userDto)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        Trainee createdTrainee = traineeMapper.convertToEntity(traineeDto);

        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertNotNull(createdTrainee),
                () -> assertEquals(traineeDto.getUser().getFirstName(), createdTrainee.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(traineeDto.getUser().getLastName(), createdTrainee.getUser().getLastName(), "check lastName"),
                () -> assertEquals(traineeDto.getUser().getUserName(), createdTrainee.getUser().getUserName(), "check userName"),
                () -> assertEquals(traineeDto.getUser().getIsActive(), createdTrainee.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(traineeDto.getDateOfBirth(), createdTrainee.getDateOfBirth(), "check date of birth"),
                () -> assertEquals(traineeDto.getAddress(), createdTrainee.getAddress(), "check address")
        );
    }

    @Test
    void convertToEntityWithNullTraineeDto() {
        Trainee trainee = traineeMapper.convertToEntity(null);
        assertNull(trainee, "ConvertToEntity: expect null when input is null");
    }

    @Test
    void convertToCreateResponse() {
        CreateResponse createResponse = traineeMapper.convertToCreateResponse(DataStorage.trainee1);

        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertNotNull(createResponse),
                () -> assertEquals(createResponse.getUserName(), DataStorage.trainee1.getUser().getUserName(), "check userName"),
                () -> assertEquals(createResponse.getPassword(), DataStorage.trainee1.getUser().getPassword(), "check password")
        );
    }

    @Test
    void convertToCreateResponseNullTraineeFail() {
        CreateResponse createResponse = traineeMapper.convertToCreateResponse(null);
        assertNull(createResponse, "ConvertToCreateResponse: expect null when input is null");
    }

    @Test
    void convertTraineeToTraineeSelectResponse() {
        User user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", "", true);

        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("Maria")
                        .lastName("Petrenko")
                        .userName("Maria.Petrenko")
                        .password("password")
                        .isActive(true)
                        .build())
                .specialization(TrainingType.builder().id(2L).trainingTypeName("yoga").build())
                .build();

        Trainee trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .trainers(List.of(trainer))
                .build();


        TraineeSelectResponse traineeSelectResponse = traineeMapper.convertTraineeToTraineeSelectResponse(trainee);

        assertNotNull(traineeSelectResponse);
        assertNotNull(traineeSelectResponse.getTrainers());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainee.getUser().getFirstName(), traineeSelectResponse.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainee.getUser().getLastName(), traineeSelectResponse.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainee.getUser().getIsActive(), traineeSelectResponse.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainee.getDateOfBirth(), traineeSelectResponse.getDateOfBirth(), "check date of birth"),
                () -> assertEquals(trainee.getAddress(), traineeSelectResponse.getAddress(), "check address"),
                //trainer
                () -> assertEquals(1, traineeSelectResponse.getTrainers().size(), "check count of trainers"),
                () -> assertEquals(trainer.getUser().getFirstName(), traineeSelectResponse.getTrainers().get(0).getUser().getFirstName(), "check trainer's firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), traineeSelectResponse.getTrainers().get(0).getUser().getLastName(), "check trainer's lastName"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), traineeSelectResponse.getTrainers().get(0).getSpecialization(), "check trainer's specialization")
        );
    }

    @Test
    void convertTraineeToTraineeSelectResponseWithoutTrainer() {
        User user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", "", true);

        Trainee trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        TraineeSelectResponse traineeSelectResponse = traineeMapper.convertTraineeToTraineeSelectResponse(trainee);

        assertNotNull(traineeSelectResponse);
        assertNull(traineeSelectResponse.getTrainers());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainee.getUser().getFirstName(), traineeSelectResponse.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainee.getUser().getLastName(), traineeSelectResponse.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainee.getUser().getIsActive(), traineeSelectResponse.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainee.getDateOfBirth(), traineeSelectResponse.getDateOfBirth(), "check date of birth"),
                () -> assertEquals(trainee.getAddress(), traineeSelectResponse.getAddress(), "check address")
        );
    }

    @Test
    void convertTraineeToTraineeSelectResponseNullTraineeFail() {
        TraineeSelectResponse traineeSelectResponse = traineeMapper.convertTraineeToTraineeSelectResponse(null);
        assertNull(traineeSelectResponse, "ConvertTraineeToTraineeSelectResponse: expect null when input is null");
    }

    @Test
    void convertTraineeToTraineeUpdateResponse() {
        User user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", "", true);

        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("Maria")
                        .lastName("Petrenko")
                        .userName("Maria.Petrenko")
                        .password("password")
                        .isActive(true)
                        .build())
                .specialization(TrainingType.builder().id(2L).trainingTypeName("yoga").build())
                .build();

        Trainee trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .trainers(List.of(trainer))
                .build();


        TraineeUpdateResponse traineeUpdateResponse = traineeMapper.convertTraineeToTraineeUpdateResponse(trainee);

        assertNotNull(traineeUpdateResponse);
        assertNotNull(traineeUpdateResponse.getTrainers());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainee.getUser().getFirstName(), traineeUpdateResponse.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainee.getUser().getLastName(), traineeUpdateResponse.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainee.getUser().getIsActive(), traineeUpdateResponse.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainee.getDateOfBirth(), traineeUpdateResponse.getDateOfBirth(), "check date of birth"),
                () -> assertEquals(trainee.getAddress(), traineeUpdateResponse.getAddress(), "check address"),
                //trainer
                () -> assertEquals(1, traineeUpdateResponse.getTrainers().size(), "check count of trainers"),
                () -> assertEquals(trainer.getUser().getFirstName(), traineeUpdateResponse.getTrainers().get(0).getUser().getFirstName(), "check trainer's firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), traineeUpdateResponse.getTrainers().get(0).getUser().getLastName(), "check trainer's lastName"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), traineeUpdateResponse.getTrainers().get(0).getSpecialization(), "check trainer's specialization")
        );
    }

    @Test
    void convertTraineeToTraineeUpdateResponseWithoutTrainer() {
        User user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", "", true);

        Trainee trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        TraineeUpdateResponse traineeUpdateResponse = traineeMapper.convertTraineeToTraineeUpdateResponse(trainee);

        assertNotNull(traineeUpdateResponse);
        assertNull(traineeUpdateResponse.getTrainers());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainee.getUser().getFirstName(), traineeUpdateResponse.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainee.getUser().getLastName(), traineeUpdateResponse.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainee.getUser().getIsActive(), traineeUpdateResponse.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainee.getDateOfBirth(), traineeUpdateResponse.getDateOfBirth(), "check date of birth"),
                () -> assertEquals(trainee.getAddress(), traineeUpdateResponse.getAddress(), "check address")
        );
    }

    @Test
    void convertTraineeToTraineeUpdateResponseNullTraineeFail() {
        TraineeUpdateResponse traineeUpdateResponse = traineeMapper.convertTraineeToTraineeUpdateResponse(null);
        assertNull(traineeUpdateResponse, "ConvertTraineeToTraineeUpdateResponse: expect null when input is null");
    }

    @Test
    void convertToString() {

        String convertToString = traineeMapper.convertToString(null);
        assertNull(convertToString, "ConvertToString: expect null when input is null");
    }

    @Test
    void convertToStringNullTrainingTypeFail() {
        String name = "Zumba";
        TrainingType trainingType = TrainingType.builder()
                .trainingTypeName(name)
                .build();
        String convertToString = traineeMapper.convertToString(trainingType);


        assertNotNull(convertToString);
        assertEquals(name, convertToString);
    }
}
