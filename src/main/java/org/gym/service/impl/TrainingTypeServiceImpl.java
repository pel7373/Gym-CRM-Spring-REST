package org.gym.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.annotation.GymService;
import org.gym.entity.TrainingType;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.TrainingTypeService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@GymService
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeRepository.findAll();
    }
}
