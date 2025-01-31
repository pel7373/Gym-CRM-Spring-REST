package org.gym.repository;

import org.gym.entity.Trainee;
import org.gym.exception.EntityNotFoundException;

import java.util.Optional;

public interface TraineeRepository {
    Optional<Trainee> findByUserName(String userName) throws EntityNotFoundException;
    Trainee save(Trainee trainee);
    void delete(String userName);
}
