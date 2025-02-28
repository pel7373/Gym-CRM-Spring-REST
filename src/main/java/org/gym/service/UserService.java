package org.gym.service;

import org.gym.dto.request.ChangeLoginRequest;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.NullEntityException;

public interface UserService {
    boolean changeStatus(String userName) throws EntityNotFoundException;
    boolean authenticate(String userName, String password);
    void changePassword(ChangeLoginRequest changeLoginRequest) throws EntityNotFoundException;

}
