package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.entity.TrainingType;
import org.gym.facade.TrainingTypeFacade;
import org.gym.service.TrainingTypeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingTypeFacadeImpl implements TrainingTypeFacade {

    private final TrainingTypeService trainingTypeService;

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeService.findAll();
    }
}
