package org.gym.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.gym.entity.Trainee;
import org.gym.entity.User;
import org.gym.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isExistsByUserName(String userName) {
        return findByUserName(userName).isPresent();
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(criteriaBuilder.equal(root.get("userName"), userName));

        try {
            return Optional.of(entityManager.createQuery(query).getSingleResult());
        } catch (NoSuchElementException | NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public User save(User user) {
        User savedUser = user;
        if (user.getId() == null) {
            entityManager.persist(user);
        } else {
            savedUser = entityManager.merge(user);
        }
        return savedUser;
    }
}
