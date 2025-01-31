package org.gym;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gym.config.Config;
import org.gym.dto.TraineeDto;
import org.gym.dto.UserDto;
import org.gym.facade.TraineeFacade;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
public class Main {
    @Transactional
    public static void main(String[] args) throws JsonProcessingException {
        System.setProperty("spring.profiles.active", "prod");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        TraineeDto traineeDto;
        TraineeDto traineeDto2;
        TraineeDto traineeDtoNotValid;
        String userNameForTrainee;


            UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
            UserDto userDto2 = new UserDto("Petro", "Ivanenko", "Petro.Ivanenko", true);
            UserDto userDtoNotValid = new UserDto("Pa", "Pa", "Maria.Petrenko2", false);

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

            traineeDtoNotValid
                    = new TraineeDto(userDtoNotValid, LocalDate.of(1995, 1, 23),
                    "Kyiv, Soborna str. 35, ap. 26");

            TraineeFacade traineeFacade = context.getBean(TraineeFacade.class);
            //TraineeDto createdTraineeDto = traineeFacade.create(traineeDto);
            traineeFacade.delete("Maria.Petrenko", "T[6iFaDNA5");
    }
}
