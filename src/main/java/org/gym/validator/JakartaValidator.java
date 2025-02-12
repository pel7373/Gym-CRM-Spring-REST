package org.gym.validator;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JakartaValidator<T> {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public boolean validate(T t) {
        return getErrors(t).isBlank();
    }

    public String getErrors(T t) {
        StringBuilder sb = new StringBuilder();
        validator.validate(t).forEach(v -> {
            sb.append(v.getMessage())
                    .append("; ");
            });
        return sb.toString();
    }



}
