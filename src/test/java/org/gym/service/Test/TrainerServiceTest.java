package org.gym.service.Test;

import org.gym.DataStorage;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingTypeDto;
import org.gym.dto.UserDto;
import org.gym.dto.request.trainer.TrainerUpdateRequest;
import org.gym.dto.request.user.UserUpdateRequest;
import org.gym.dto.response.trainer.TrainerUpdateResponse;
import org.gym.dto.response.user.UserUpdateResponse;
import org.gym.entity.*;
import org.gym.entity.Trainer;
import org.gym.exception.EntityNotFoundException;
import org.gym.mapper.TrainerMapper;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.UserNameGeneratorService;
import org.gym.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerServiceTest {
    
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private UserNameGeneratorService userNameGeneratorService;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private final DataStorage ds = new DataStorage();

    private TrainerDto trainerDto;
    private Trainer trainer;
    private String userNameForTrainerDto;
    private TrainingType trainerTrainingType;
    private TrainingTypeDto trainerTrainingTypeDto;
    private final TrainingType trainingType = TrainingType.builder()
            .trainingTypeName("Zumba")
            .build();

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        String passwordForUser = "AAAAAAAAAA";
        User user = new User(null, "Maria", "Petrenko", "Maria.Petrenko", passwordForUser, true);

        trainerDto = TrainerDto.builder()
                .user(userDto)
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        trainer = Trainer.builder()
                .user(user)
                .specialization(TrainingType.builder()
                        .trainingTypeName("Zumba")
                        .build())
                .build();

        trainerTrainingType = trainer.getSpecialization();
        trainerTrainingTypeDto = trainerDto.getSpecialization();
        userNameForTrainerDto = trainerDto.getUser().getUserName();
    }

    @Test
    void createTrainerSuccessfully() {
        when(trainerRepository.save(ds.trainer1)).thenReturn(ds.trainer1);
        when(trainingTypeRepository.findByName(ds.trainerTrainingTypeName))
                .thenReturn(Optional.ofNullable(ds.trainerTrainingType));
        when(userNameGeneratorService.generate(anyString(), anyString()))
                .thenReturn(ds.trainerDto1.getUser().getUserName());
        when(passwordGeneratorService.generate()).thenReturn(ds.trainer1.getUser().getPassword());
        when(trainerMapper.convertToEntity(ds.trainerDto1)).thenReturn(ds.trainer1);
        when(trainerMapper.convertToCreateResponse(ds.trainer1)).thenReturn(ds.trainerCreateResponse);

        trainerService.create(ds.trainerDto1);

        verify(trainerRepository, times(1)).save(any(Trainer.class));
        verify(trainingTypeRepository, times(1)).findByName(any(String.class));
        verify(trainerMapper, times(1)).convertToEntity(ds.trainerDto1);
        verify(trainerMapper, times(1)).convertToCreateResponse(ds.trainer1);
        verify(userNameGeneratorService, times(1)).generate(any(String.class), any(String.class));
        verify(passwordGeneratorService, times(1)).generate();
    }

    @Test
    void createTrainerNotValidTrainingTypeFail() {
        when(trainerRepository.save(ds.trainer1)).thenReturn(ds.trainer1);
        when(trainingTypeRepository.findByName(ds.trainerTrainingTypeName))
                .thenReturn(Optional.empty());
        when(userNameGeneratorService.generate(anyString(), anyString()))
                .thenReturn(ds.trainerDto1.getUser().getUserName());
        when(passwordGeneratorService.generate()).thenReturn(ds.trainer1.getUser().getPassword());
        when(trainerMapper.convertToEntity(ds.trainerDto1)).thenReturn(ds.trainer1);
        when(trainerMapper.convertToCreateResponse(ds.trainer1)).thenReturn(ds.trainerCreateResponse);

        assertThrows(EntityNotFoundException.class, () -> trainerService.create(ds.trainerDto1));

        verify(trainerRepository, times(0)).save(any(Trainer.class));
        verify(trainingTypeRepository, times(1)).findByName(any(String.class));
        verify(trainerMapper, times(0)).convertToEntity(ds.trainerDto1);
        verify(trainerMapper, times(0)).convertToCreateResponse(ds.trainer1);
        verify(userNameGeneratorService, times(1)).generate(any(String.class), any(String.class));
        verify(passwordGeneratorService, times(0)).generate();
    }


    @Test
    void selectTrainerUserNameNotFoundFail() {
        userNameForTrainerDto = "NotFound";
        when(trainerRepository.findByUserName(userNameForTrainerDto)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainerService.select(userNameForTrainerDto));

        verify(trainerMapper, times(0)).convertToTrainerSelectResponse(any(Trainer.class));
        verify(trainerRepository, times(1)).findByUserName(userNameForTrainerDto);
    }

    @Test
    void selectTrainerSuccessfully() {
        userNameForTrainerDto = trainerDto.getUser().getFirstName();
        when(trainerRepository.findByUserName(userNameForTrainerDto)).thenReturn(Optional.ofNullable(trainer));

        trainerService.select(userNameForTrainerDto);

        verify(trainerMapper, times(1)).convertToTrainerSelectResponse(any(Trainer.class));
        verify(trainerRepository, times(1)).findByUserName(userNameForTrainerDto);
    }

    @Test
    void updateExistingTrainerSuccessfully() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("John", "Doe", true);
        TrainerUpdateRequest trainerUpdateRequest = TrainerUpdateRequest.builder()
                .user(userUpdateRequest)
                .specialization(trainerTrainingTypeDto)
                .build();

        User userForUpdate = new User(2L, "Maria", "Ivanova", "Maria.Ivanova", "BBBBBBBBBB", true);
        Trainer trainerForUpdate = Trainer.builder()
                .id(2L)
                .user(userForUpdate)
                .specialization(trainerTrainingType)
                .build();

        User userUpdated = new User(2L, "John", "Doe", "Maria.Ivanova", "BBBBBBBBBB", true);
        Trainer trainerUpdated = Trainer.builder()
                .id(2L)
                .user(userUpdated)
                .specialization(trainerTrainingType)
                .build();

        UserUpdateResponse userUpdateResponse = new UserUpdateResponse("John", "Doe", "Maria.Ivanova", true);
        TrainerUpdateResponse trainerUpdateResponse = TrainerUpdateResponse.builder()
                .user(userUpdateResponse)
                .specialization(trainerTrainingTypeDto.getTrainingTypeName())
                .build();


        when(trainerRepository.findByUserName(userForUpdate.getUserName()))
                .thenReturn(Optional.ofNullable(trainerForUpdate));
        when(trainerRepository.save(trainerUpdated)).thenReturn(trainerUpdated);
        when(trainerMapper.convertTrainerToTrainerUpdateResponse(trainerUpdated)).thenReturn(trainerUpdateResponse);
        when(trainingTypeRepository.findByName(trainerTrainingTypeDto.getTrainingTypeName()))
                .thenReturn(Optional.ofNullable(trainerTrainingType));

        TrainerUpdateResponse trainerUpdateResponseActual = trainerService.update(userForUpdate.getUserName(), trainerUpdateRequest);

        assertAll(
                "Grouped assertions of selected trainerDto",
                () -> assertNotNull(trainerUpdateResponseActual),
                () -> assertEquals(trainerUpdateResponse.getUser().getFirstName(),
                trainerUpdateResponseActual.getUser().getFirstName(), "firstName should be Maria"),
                () -> assertEquals(trainerUpdateResponse.getUser().getLastName(),
                        trainerUpdateResponseActual.getUser().getLastName(), "lastName should be Petrenko"),
                () -> assertEquals(trainerUpdateResponse.getSpecialization(),
                        trainerUpdateResponseActual.getSpecialization(), "specialization should be equal")
        );

        verify(trainerRepository, times(1)).findByUserName("Maria.Ivanova");
        verify(trainerRepository, times(1)).save(trainerUpdated);
        verify(userNameGeneratorService, never())
                .generate(any(String.class), any(String.class));
        verify(trainerMapper, times(1)).convertTrainerToTrainerUpdateResponse(any(Trainer.class));

        verify(trainingTypeRepository, times(1))
                .findByName(trainerTrainingTypeDto.getTrainingTypeName());
        verify(trainerMapper, never()).convertToEntity(trainerDto);
    }

    @Test
    void updateTrainerNotFoundFail() {
        String userNameNotFound = "NotFound";
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("John", "Doe", true);
        TrainerUpdateRequest trainerUpdateRequest = TrainerUpdateRequest.builder()
                .user(userUpdateRequest)
                .specialization(trainerTrainingTypeDto)
                .build();

        User userUpdated = new User(2L, "John", "Doe", "Maria.Ivanova", "BBBBBBBBBB", true);
        Trainer trainerUpdated = Trainer.builder()
                .id(2L)
                .user(userUpdated)
                .specialization(trainerTrainingType)
                .build();

        UserUpdateResponse userUpdateResponse = new UserUpdateResponse("John", "Doe", "Maria.Ivanova", true);
        TrainerUpdateResponse trainerUpdateResponse = TrainerUpdateResponse.builder()
                .user(userUpdateResponse)
                .specialization(trainerTrainingTypeDto.getTrainingTypeName())
                .build();


        when(trainerRepository.findByUserName(userNameNotFound))
                .thenReturn(Optional.empty());
        when(trainerRepository.save(trainerUpdated)).thenReturn(trainerUpdated);
        when(trainerMapper.convertTrainerToTrainerUpdateResponse(trainerUpdated)).thenReturn(trainerUpdateResponse);
        when(trainingTypeRepository.findByName(trainerTrainingTypeDto.getTrainingTypeName()))
                .thenReturn(Optional.ofNullable(trainerTrainingType));

        assertThrows(EntityNotFoundException.class, () -> trainerService.update(userNameNotFound, trainerUpdateRequest));

        verify(trainerRepository, times(1)).findByUserName(userNameNotFound);
        verify(trainerRepository, times(0)).save(trainerUpdated);
        verify(userNameGeneratorService, never())
                .generate(any(String.class), any(String.class));
        verify(trainerMapper, times(0)).convertTrainerToTrainerUpdateResponse(any(Trainer.class));

        verify(trainingTypeRepository, times(0))
                .findByName(trainerTrainingTypeDto.getTrainingTypeName());
        verify(trainerMapper, never()).convertToEntity(trainerDto);
    }

    @Test
    void updateTrainerTrainingTypeNotFoundFail() {
        TrainingTypeDto trainingTypeDtoNotFound = TrainingTypeDto.builder()
                .trainingTypeName("NotFound")
                .build();

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("John", "Doe", true);
        TrainerUpdateRequest trainerUpdateRequest = TrainerUpdateRequest.builder()
                .user(userUpdateRequest)
                .specialization(trainingTypeDtoNotFound)
                .build();

        User userForUpdate = new User(2L, "Maria", "Ivanova", "Maria.Ivanova", "BBBBBBBBBB", true);
        Trainer trainerForUpdate = Trainer.builder()
                .id(2L)
                .user(userForUpdate)
                .specialization(trainerTrainingType)
                .build();

        User userUpdated = new User(2L, "John", "Doe", "Maria.Ivanova", "BBBBBBBBBB", true);
        Trainer trainerUpdated = Trainer.builder()
                .id(2L)
                .user(userUpdated)
                .specialization(trainerTrainingType)
                .build();

        UserUpdateResponse userUpdateResponse = new UserUpdateResponse("John", "Doe", "Maria.Ivanova", true);
        TrainerUpdateResponse trainerUpdateResponse = TrainerUpdateResponse.builder()
                .user(userUpdateResponse)
                .specialization(trainerTrainingTypeDto.getTrainingTypeName())
                .build();


        when(trainerRepository.findByUserName(userForUpdate.getUserName()))
                .thenReturn(Optional.ofNullable(trainerForUpdate));
        when(trainerRepository.save(trainerUpdated)).thenReturn(trainerUpdated);
        when(trainerMapper.convertTrainerToTrainerUpdateResponse(trainerUpdated)).thenReturn(trainerUpdateResponse);
        when(trainingTypeRepository.findByName(trainerTrainingTypeDto.getTrainingTypeName()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainerService.update(userForUpdate.getUserName(), trainerUpdateRequest));

        verify(trainerRepository, times(1)).findByUserName("Maria.Ivanova");
        verify(trainerRepository, times(0)).save(trainerUpdated);
        verify(userNameGeneratorService, never())
                .generate(any(String.class), any(String.class));
        verify(trainerMapper, times(0)).convertTrainerToTrainerUpdateResponse(any(Trainer.class));

        verify(trainingTypeRepository, times(1))
                .findByName("NotFound");
        verify(trainerMapper, never()).convertToEntity(trainerDto);
    }


    @Test
    void changeSpecializationSuccessfully() {
        when(trainerRepository.findByUserName(userNameForTrainerDto)).thenReturn(Optional.ofNullable(trainer));

        trainerService.changeSpecialization(userNameForTrainerDto, trainingType);

        verify(trainerRepository, times(1)).findByUserName(userNameForTrainerDto);
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }
}
