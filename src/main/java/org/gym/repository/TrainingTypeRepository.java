package org.gym.repository;

import org.gym.entity.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository {
    Optional<TrainingType> findByName(String name);
    List<TrainingType> findAll();
}
