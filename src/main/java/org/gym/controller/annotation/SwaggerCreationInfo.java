package org.gym.controller.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation
public @interface SwaggerCreationInfo {
    String summary();
    String description();
    Class<?> schema() default Void.class;
    ApiResponse[] additionalResponses() default {};
}
