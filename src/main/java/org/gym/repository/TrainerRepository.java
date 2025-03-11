package org.gym.repository;

import org.gym.entity.Trainer;
import org.gym.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository {
    Optional<Trainer> findByUserName(String userName) throws EntityNotFoundException;
    Trainer save(Trainer trainer);
    List<Trainer> findAll();
}
