package org.gym.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import java.security.SecureRandom;

@Slf4j
@Configuration
@ComponentScan(basePackages = "org.gym")
@PropertySource("classpath:application.properties")
public class Config {
    public static final String ENTITY_CANT_BE_NULL_OR_BLANK = "entity can't be null or blank";
    public static final String ACCESS_DENIED = "access denied to {}";
    public static final String USERNAME_PASSWORD_CANT_BE_NULL_OR_BLANK = "userName or/and password can't be null or blank";
    public static final String USERNAME_CANT_BE_NULL_OR_BLANK = "userName can't be null or blank";
    public static final String ENTITY_NOT_FOUND = "entity not found by userName {}";
    public static final String ENTITY_NOT_FOUND_EXCEPTION = "entity not found by userName %s";

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }
}
