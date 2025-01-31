package org.gym.validator;

import org.gym.dto.TrainingDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class TrainingDtoValidator extends EntityValidator<TrainingDto> {

    @Value("${validator.minNamesLength:5}")
    private Integer minNamesLength;

    @Value("${validator.trainingNameRegex:[A-Z0-9][\\sa-z0-9.:-=]+$}")
    private String trainingNameRegex;

    @Value("${validator.minTrainingDuration:15}")
    private Integer minTrainingDuration;

    @Override
    Validator getValidator() {
        return Validator.forInstanceOf(TrainingDto.class, (t, errors) -> {
            if (t.getTrainingName() == null || t.getTrainingName().length() < minNamesLength) {
                errors.rejectValue("trainingName", "field.min.length", new Object[]{},
                        String.format("the trainingName can't be blank or less than %d chars", minNamesLength));
            } else if(!t.getTrainingName().matches(trainingNameRegex)) {
                errors.rejectValue("trainingName", "field.not.valid", new Object[]{},
                        String.format("the trainingName (%s) must comply with trainingName's rules", t.getTrainingName()));
            }

            if (t.getDate() == null) {
                errors.rejectValue("date", "field.date.notNull", new Object[]{},
                        "the trainingDate can't be null");
            }

            if (t.getDuration() == null || t.getDuration() < minTrainingDuration) {
                errors.rejectValue("duration", "field.duration.notNull.length", new Object[]{},
                        String.format("the trainingDuration can't be null or less than %d", minTrainingDuration));
            }
        });
    }
}
