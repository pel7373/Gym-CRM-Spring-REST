package org.gym.service;

import org.gym.config.Config;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebAppConfiguration
public class UserServiceIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    //    @Test
//    void changeStatusSuccessfully() {
//        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
//        userNameForTrainer = createdTrainerDto.getUser().getUserName();
//
//        assertNotNull(createdTrainerDto);
//        assertNotNull(createdTrainerDto.getUser());
//
//        boolean oldStatus = createdTrainerDto.getUser().getIsActive();
//        boolean newStatus = !oldStatus;
//        TrainerDto changedTrainerDto = trainerService.changeStatus(userNameForTrainer, newStatus);
//
//        assertNotNull(changedTrainerDto);
//        assertNotNull(changedTrainerDto.getUser());
//        assertEquals(newStatus, changedTrainerDto.getUser().getIsActive());
//    }

    //
//    @Test
//    void changePasswordSuccessfully() {
//        TrainerDto createdTrainerDto = trainerService.create(trainerDto);
//        userNameForTrainer = createdTrainerDto.getUser().getUserName();
//
//        assertNotNull(createdTrainerDto);
//        assertNotNull(createdTrainerDto.getUser());
//
//        String newPassword = "1111111111";
//
//        TrainerDto changedTrainerDto = trainerService.changePassword(userNameForTrainer, newPassword);
//        String changedPassword = trainerRepository.findByUserName(userNameForTrainer).get().getUser().getPassword();
//
//        assertNotNull(changedTrainerDto);
//        assertNotNull(changedTrainerDto.getUser());
//        assertEquals(newPassword, changedPassword);
//    }

}
