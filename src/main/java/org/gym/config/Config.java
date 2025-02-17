package org.gym.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Slf4j
@Configuration
@ComponentScan(basePackages = "org.gym")
@PropertySource("classpath:application.properties")
public class Config {
    public static final String ENTITY_CANT_BE_NULL_OR_BLANK = "Entity can't be null or blank";
    public static final String USERNAME_PASSWORD_CANT_BE_NULL_OR_BLANK = "UserName or/and password can't be null or blank";
    public static final String USERNAME_CANT_BE_NULL_OR_BLANK = "UserName can't be null or blank";
    public static final String ENTITY_NOT_FOUND = "Entity not found by {}";
    public static final String ENTITY_NOT_FOUND_EXCEPTION = "Entity not found by %s";
    public static final String ENTITY_NOT_VALID_EXCEPTION = "Entity not valid. Error message %s";
    public static final String ENTITY_NOT_VALID = "Entity not valid. Error message {}";


    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
        objectMapper.setDateFormat(df);
        return objectMapper;
    }
}
