package org.gym.controller.IT;

import org.gym.DataStorage;
import org.gym.config.Config;
import org.gym.controller.TrainingTypeController;
import org.gym.dto.response.trainingType.TrainingTypeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@ActiveProfiles("prod")
@WebAppConfiguration
class TrainingTypeTestContainerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQL10Dialect");
        registry.add("hibernate.hbm2ddl.auto", () -> "create");
        registry.add("hibernate.show_sql", () -> true);
        registry.add("hibernate.format_sql", () -> true);
        registry.add("hibernate.jdbc.lob.non_contextual_creation", () -> true);
    }

    @Autowired
    private TrainingTypeController trainingTypeController;

    private final DataStorage ds = new DataStorage();

    @Test
    void getAll() {
        List<TrainingTypeResponse> trainingTypeResponseList = trainingTypeController.getAll();

        assertEquals(ds.expectedTrainingTypeNamesList.size(), trainingTypeResponseList.size());

        trainingTypeResponseList
                .forEach(t ->
                        assertTrue(ds.expectedTrainingTypeNamesList.contains(t.getTrainingTypeName()), String.format("trainingTypeList contains %s name", t.getTrainingTypeName())));
    }
}
