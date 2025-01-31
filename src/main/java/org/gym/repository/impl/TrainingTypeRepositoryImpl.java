package org.gym.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.gym.entity.TrainingType;
import org.gym.repository.TrainingTypeRepository;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<TrainingType> findByName(String trainingTypeName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingType> criteriaQuery = criteriaBuilder.createQuery(TrainingType.class);
        Root<TrainingType> root = criteriaQuery.from(TrainingType.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(
                root.get("trainingTypeName"), trainingTypeName));

        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoSuchElementException | NoResultException e) {
            return Optional.empty();
        }
    }
}
