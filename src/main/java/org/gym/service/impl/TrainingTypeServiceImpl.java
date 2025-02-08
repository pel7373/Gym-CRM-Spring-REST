package org.gym.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.entity.TrainingType;
import org.gym.repository.TrainingTypeRepository;
import org.gym.service.TrainingTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeRepository.findAll();
    }
}
