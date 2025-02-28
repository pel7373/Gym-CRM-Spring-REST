package org.gym.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
@TestPropertySource(locations = "classpath:application-test.properties")
//@ContextConfiguration(classes = {DbConfig.class, Config.class})
@ComponentScan(basePackages = {"org.gym"}
        ,
        excludeFilters={
                @ComponentScan.Filter(type= FilterType.ANNOTATION, value= EnableWebMvc.class)
        }
)
public class TestServiceConfig {
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//        return dataSource;
//    }
//
//    public static final String ENTITY_NOT_FOUND = "Entity not found by {}";
//    public static final String ENTITY_NOT_FOUND_EXCEPTION = "Entity not found by %s";
//    public static final String ACCESS_DENIED = "access denied to {}";
//    public static final String ACCESS_DENIED_EXCEPTION = "access denied to %s";
//
//    @Bean
//    public SecureRandom secureRandom() {
//        return new SecureRandom();
//    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
//        objectMapper.setDateFormat(df);
//        return objectMapper;
//    }
}
