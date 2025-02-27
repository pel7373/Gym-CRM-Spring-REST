package org.gym.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.gym.dto.TraineeTrainingsDto;
import org.gym.dto.TrainerTrainingsDto;
import org.gym.repository.TrainingRepository;
import org.gym.entity.Training;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrainingRepositoryImpl implements TrainingRepository {

    public static final String USER_NAME = "userName";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Training save(Training training) {
        Training savedTraining = training;
        if (training.getId() == null) {
            entityManager.persist(training);
        } else {
            savedTraining = entityManager.merge(training);
        }
        return savedTraining;
    }

    @Override
    public List<Training> getByTraineeCriteria(TraineeTrainingsDto traineeTrainingsDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> criteriaQuery = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = criteriaQuery.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("trainee").get("user").get(USER_NAME), traineeTrainingsDto.getTraineeUserName()));

        if(traineeTrainingsDto.getFromDate() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), traineeTrainingsDto.getFromDate()));
        }

        if(traineeTrainingsDto.getToDate() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), traineeTrainingsDto.getToDate()));
        }

        if(traineeTrainingsDto.getTrainerUserName() != null) {
            predicates.add(criteriaBuilder.equal(root.get("trainer").get("user").get(USER_NAME), traineeTrainingsDto.getTrainerUserName()));
        }

        if(traineeTrainingsDto.getTrainingType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("trainingType").get("trainingTypeName"), traineeTrainingsDto.getTrainingType()));
        }

        criteriaQuery.select(root).where(predicates.toArray(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Training> getByTrainerCriteria(TrainerTrainingsDto trainerTrainingsDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> criteriaQuery = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = criteriaQuery.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("trainer").get("user").get(USER_NAME), trainerTrainingsDto.getTrainerUserName()));

        if(trainerTrainingsDto.getFromDate() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), trainerTrainingsDto.getFromDate()));
        }

        if(trainerTrainingsDto.getToDate() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), trainerTrainingsDto.getToDate()));
        }

        if(trainerTrainingsDto.getTraineeUserName() != null) {
            predicates.add(criteriaBuilder.equal(root.get("trainee").get("user").get(USER_NAME), trainerTrainingsDto.getTraineeUserName()));
        }

        criteriaQuery.select(root).where(predicates.toArray(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
