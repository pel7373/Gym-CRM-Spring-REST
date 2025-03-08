package org.gym.mapper;

import org.gym.DataStorage;
import org.gym.config.TestConfig;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.dto.response.CreateResponse;
import org.gym.dto.response.trainer.TrainerForListResponse;
import org.gym.dto.response.trainer.TrainerSelectResponse;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
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
class TrainerMapperTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrainerMapper trainerMapper;

    private final DataStorage ds = new DataStorage();

    @Test
    void convertToDto() {
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

        TrainerDto createdTrainerDto = trainerMapper.convertToDto(trainer);

        assertNotNull(createdTrainerDto);
        assertAll(
                "Grouped assertions of created trainerDto",
                () -> assertEquals(trainer.getUser().getFirstName(), createdTrainerDto.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), createdTrainerDto.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainer.getUser().getUserName(), createdTrainerDto.getUser().getUserName(), "check userName"),
                () -> assertEquals(trainer.getUser().getIsActive(), createdTrainerDto.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), createdTrainerDto.getSpecialization().getTrainingTypeName(), "check specialization")
        );
    }

    @Test
    void convertToDtoWithNullTrainer() {
        TrainerDto trainerDto = trainerMapper.convertToDto(null);
        assertNull(trainerDto, "ConvertToDto: null when input is null");
    }

    @Test
    void convertToEntity() {
        TrainerDto trainerDto = TrainerDto.builder()
                .user(UserDto.builder()
                        .firstName("Maria")
                        .lastName("Petrenko")
                        .userName("Maria.Petrenko")
                        .isActive(true)
                        .build())
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("yoga")
                        .build())
                .build();

        Trainer createdTrainer = trainerMapper.convertToEntity(trainerDto);

        assertNotNull(createdTrainer);
        assertAll(
                "Grouped assertions of created trainerDto",
                () -> assertEquals(trainerDto.getUser().getFirstName(), createdTrainer.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainerDto.getUser().getLastName(), createdTrainer.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainerDto.getUser().getUserName(), createdTrainer.getUser().getUserName(), "check userName"),
                () -> assertEquals(trainerDto.getUser().getIsActive(), createdTrainer.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainerDto.getSpecialization().getTrainingTypeName(), createdTrainer.getSpecialization().getTrainingTypeName(), "check specialization")
        );
    }

    @Test
    void convertToEntityWithNullTrainerDto() {
        Trainer trainer = trainerMapper.convertToEntity(null);
        assertNull(trainer, "ConvertToEntity: null when input is null");
    }

    @Test
    void convertToCreateResponse() {
        CreateResponse createResponse = trainerMapper.convertToCreateResponse(ds.trainer1);

        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertNotNull(createResponse),
                () -> assertEquals(createResponse.getUserName(), ds.trainer1.getUser().getUserName(), "check userName"),
                () -> assertEquals(createResponse.getPassword(), ds.trainer1.getUser().getPassword(), "check password")
        );
    }

    @Test
    void convertToCreateResponseNullTraineeFail() {
        CreateResponse createResponse = trainerMapper.convertToCreateResponse(null);
        assertNull(createResponse, "ConvertToCreateResponse: expect null when input is null");
    }

    @Test
    void convertTrainerToTrainerSelectResponse() {
        User user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", "", true);

        Trainee trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("Maria")
                        .lastName("Petrenko")
                        .userName("Maria.Petrenko")
                        .password("password")
                        .isActive(true)
                        .build())
                .specialization(TrainingType.builder().id(2L).trainingTypeName("yoga").build())
                .trainees(List.of(trainee))
                .build();

        TrainerSelectResponse trainerSelectResponse = trainerMapper.convertToTrainerSelectResponse(trainer);

        assertNotNull(trainerSelectResponse);
        assertNotNull(trainerSelectResponse.getTrainees());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainer.getUser().getFirstName(), trainerSelectResponse.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), trainerSelectResponse.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainer.getUser().getIsActive(), trainerSelectResponse.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), trainerSelectResponse.getSpecialization(), "check specialization"),
                //trainee
                () -> assertEquals(1, trainerSelectResponse.getTrainees().size(), "check count of trainers"),
                () -> assertEquals(trainee.getUser().getFirstName(), trainerSelectResponse.getTrainees().get(0).getUser().getFirstName(), "check trainer's firstName"),
                () -> assertEquals(trainee.getUser().getLastName(), trainerSelectResponse.getTrainees().get(0).getUser().getLastName(), "check trainer's lastName")
        );
    }

    @Test
    void convertTrainerToTrainerSelectResponseWithoutTrainee() {
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

        TrainerSelectResponse trainerSelectResponse = trainerMapper.convertToTrainerSelectResponse(trainer);

        assertNotNull(trainerSelectResponse);
        assertNull(trainerSelectResponse.getTrainees());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainer.getUser().getFirstName(), trainerSelectResponse.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), trainerSelectResponse.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainer.getUser().getIsActive(), trainerSelectResponse.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), trainerSelectResponse.getSpecialization(), "check specialization of birth")
        );
    }

    @Test
    void convertTrainerToTrainerSelectResponseNullTrainerFail() {
        TrainerSelectResponse trainerSelectResponse = trainerMapper.convertToTrainerSelectResponse(null);
        assertNull(trainerSelectResponse, "ConvertTrainerToTrainerSelectResponse: expect null when input is null");
    }

    @Test
    void convertTrainerToTrainerUpdateResponse() {
        User user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", "", true);

        Trainee trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("Maria")
                        .lastName("Petrenko")
                        .userName("Maria.Petrenko")
                        .password("password")
                        .isActive(true)
                        .build())
                .specialization(TrainingType.builder().id(2L).trainingTypeName("yoga").build())
                .trainees(List.of(trainee))
                .build();

        TrainerUpdateResponse trainerUpdateResponse = trainerMapper.convertTrainerToTrainerUpdateResponse(trainer);

        assertNotNull(trainerUpdateResponse);
        assertNotNull(trainerUpdateResponse.getTrainees());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainer.getUser().getFirstName(), trainerUpdateResponse.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), trainerUpdateResponse.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainer.getUser().getIsActive(), trainerUpdateResponse.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), trainerUpdateResponse.getSpecialization(), "check trainer's specialization"),
                //trainer
                () -> assertEquals(1, trainerUpdateResponse.getTrainees().size(), "check count of trainers"),
                () -> assertEquals(trainer.getUser().getFirstName(), trainerUpdateResponse.getTrainees().get(0).getUser().getFirstName(), "check trainer's firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), trainerUpdateResponse.getTrainees().get(0).getUser().getLastName(), "check trainer's lastName")
        );
    }

    @Test
    void convertTrainerToTrainerUpdateResponseWithoutTrainee() {
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

        TrainerUpdateResponse trainerUpdateResponse = trainerMapper.convertTrainerToTrainerUpdateResponse(trainer);

        assertNotNull(trainerUpdateResponse);
        assertNull(trainerUpdateResponse.getTrainees());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainer.getUser().getFirstName(), trainerUpdateResponse.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), trainerUpdateResponse.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainer.getUser().getIsActive(), trainerUpdateResponse.getUser().getIsActive(), "check isActive"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), trainerUpdateResponse.getSpecialization(), "check trainer's specialization")
        );
    }

    @Test
    void convertTrainerToTrainerUpdateResponseNullTrainerFail() {
        TrainerUpdateResponse trainerUpdateResponse = trainerMapper.convertTrainerToTrainerUpdateResponse(null);
        assertNull(trainerUpdateResponse, "ConvertTrainerToTrainerUpdateResponse: expect null when input is null");
    }

    @Test
    void convertTrainerToTrainerForListResponse() {
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

        TrainerForListResponse trainerForListResponse = trainerMapper.convertTrainerToTrainerForListResponse(trainer);

        assertNotNull(trainerForListResponse);
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainer.getUser().getFirstName(), trainerForListResponse.getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), trainerForListResponse.getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainer.getUser().getUserName(), trainerForListResponse.getUser().getUserName(), "check lastName"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), trainerForListResponse.getSpecialization(), "check trainer's specialization")
        );
    }

    @Test
    void convertTrainerToTrainerForListResponseNullTrainerFail() {
        TrainerForListResponse trainerForListResponse = trainerMapper.convertTrainerToTrainerForListResponse(null);
        assertNull(trainerForListResponse, "convertTrainerToTrainerForListResponse: expect null when input is null");
    }

    @Test
    void convertTrainerListToTrainerForListResponseList() {
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

        List<TrainerForListResponse> trainerForListResponses = trainerMapper.convertTrainerListToTrainerForListResponseList(List.of(trainer));

        assertNotNull(trainerForListResponses);
        assertEquals(1, trainerForListResponses.size());
        assertAll(
                "Grouped assertions of created traineeDto",
                () -> assertEquals(trainer.getUser().getFirstName(), trainerForListResponses.get(0).getUser().getFirstName(), "check firstName"),
                () -> assertEquals(trainer.getUser().getLastName(), trainerForListResponses.get(0).getUser().getLastName(), "check lastName"),
                () -> assertEquals(trainer.getUser().getUserName(), trainerForListResponses.get(0).getUser().getUserName(), "check lastName"),
                () -> assertEquals(trainer.getSpecialization().getTrainingTypeName(), trainerForListResponses.get(0).getSpecialization(), "check trainer's specialization")
        );
    }

    @Test
    void convertTrainerListToTrainerForListResponseListNullListFail() {
        List<TrainerForListResponse> trainerForListResponses = trainerMapper.convertTrainerListToTrainerForListResponseList(null);
        assertNull(trainerForListResponses, "convertTrainerListToTrainerForListResponseList: expect null when input is null");
    }

}
