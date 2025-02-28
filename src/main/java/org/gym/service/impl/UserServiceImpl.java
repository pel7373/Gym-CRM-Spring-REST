package org.gym.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.annotation.GymService;
import org.gym.dto.request.ChangeLoginRequest;
import org.gym.entity.User;
import org.gym.exception.AccessDeniedException;
import org.gym.exception.EntityNotFoundException;
import org.gym.repository.UserRepository;
import org.gym.service.UserService;

import static org.gym.config.Config.*;

@Slf4j
@AllArgsConstructor
@GymService
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean changeStatus(String userName) throws EntityNotFoundException {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
                );
        user.setIsActive(!user.getIsActive());
        return user.getIsActive();
    }

    @Override
    public boolean authenticate(String userName, String password) {
        try {
            User user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format(ENTITY_NOT_FOUND_EXCEPTION, userName))
                    );
            return user.getPassword().equals(password);
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    @Override
    public void changePassword(ChangeLoginRequest changeLoginRequest) throws EntityNotFoundException {
        if(!authenticate(changeLoginRequest.getUserName(), changeLoginRequest.getOldPassword())) {
            LOGGER.warn(ACCESS_DENIED, changeLoginRequest.getUserName());
            throw new AccessDeniedException(String.format(ACCESS_DENIED_EXCEPTION, changeLoginRequest.getUserName()));
        }

        User user = userRepository.findByUserName(changeLoginRequest.getUserName())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND_EXCEPTION, changeLoginRequest.getUserName()))
                );
        user.setPassword(changeLoginRequest.getNewPassword());
    }
}
