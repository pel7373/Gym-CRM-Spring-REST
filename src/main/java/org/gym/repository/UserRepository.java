package org.gym.repository;

import org.gym.entity.User;

import java.util.Optional;

public interface UserRepository {
    boolean isExistsByUserName(String userName);
    Optional<User> findByUserName(String userName);
    User save(User user);
}
