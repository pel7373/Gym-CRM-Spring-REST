package org.gym.validator;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

public abstract class EntityValidator<T> {

    private Validator validator = getValidator();

    public boolean validate(T t) {
        return !validator.validateObject(t).hasErrors();
    }

    public String getErrorMessage(T t) {
        Errors errors = validator.validateObject(t);
        if(errors.hasErrors()) {
            List<ObjectError> allErrors = errors.getAllErrors();
            return  allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("; ", "[ Entity isn't valid! Errors " + errors.getErrorCount() + ": ", " ]"));
        }
        return "";
    }

   abstract Validator getValidator();
}
