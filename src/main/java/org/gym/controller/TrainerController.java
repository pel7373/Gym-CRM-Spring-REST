package org.gym.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.*;
import org.gym.entity.Trainer;
import org.gym.facade.TrainerFacade;
import org.gym.mapper.TrainerMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainers")
@Validated
public class TrainerController {

    private final TrainerFacade trainerFacade;
    //private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final TransactionIdGenerator transactionIdGenerator;

    //    @SwaggerCreationInfo(
//            summary = "Create a trainee",
//            description = "Adds a new trainee",
//            schema = TraineeCreateResponse.class
    @PostMapping//(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TrainerCreateResponse create(@RequestBody @Valid TrainerDto trainerDto){
        String id = transactionIdGenerator.generate();
        TrainerCreateResponse response = trainerFacade.create(trainerDto);
        LOGGER.info("request {}, response {}, id {}", trainerDto, response, id);
        return response;
    }

    @PostMapping(value = "1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TrainerGetProfileResponse create2(@RequestBody @Valid TrainerDto trainerDto){
        String id = transactionIdGenerator.generate();
        //TrainerDto trainerDto = trainerMapper.convertCreateRequestToDto(request);
        LOGGER.info("request {}, id {}", trainerDto, id);
        Trainer trainer = trainerMapper.convertToEntity(trainerDto);
        LOGGER.info("trainer {}, id {}", trainer, id);

        TrainerGetProfileResponse trainerGetProfileResponse = trainerMapper.convertToTrainerGetProfileResponse(trainerDto);
        LOGGER.info("trainerProfileResponse {}, id {}", trainerGetProfileResponse, id);
        return trainerGetProfileResponse;
        //TrainerCreateResponse response = trainerFacade.create(trainerDto);

//        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", "",true);
//
//        TrainerDto trainerDto2 = TrainerDto.builder()
//                .user(userDto)
//                .specialization(TrainingTypeDto.builder()
//                        .trainingTypeName("Zumba")
//                        .build())
//                .build();
//        LOGGER.info("id {}, request {}, response {}", id,  trainerDto, response);
//
        //return trainerProfileResponse;
    }

    @GetMapping("login/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> login(@PathVariable("username") String userName,
                                      @PathVariable("password") String password) {
        String id = transactionIdGenerator.generate();
        boolean response = trainerFacade.authenticate(userName, password);
        LOGGER.info("login: id {}, userName {}, response {}", id, userName, response);
        if(response) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
