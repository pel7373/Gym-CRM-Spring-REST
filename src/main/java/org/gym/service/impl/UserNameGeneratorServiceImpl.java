package org.gym.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.exception.NullEntityException;

import org.gym.repository.UserRepository;
import org.gym.service.UserNameGeneratorService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class UserNameGeneratorServiceImpl implements UserNameGeneratorService {

    private final UserRepository userRepository;

    @Override
    public String generate(String firstName, String lastName) throws NullEntityException {
        if(isFirstAndLastNamesNullOrBlank(firstName, lastName)) {
            throw new NullEntityException("generate: firstName or/and lastName can't be null, blank or empty");
        }

        String userName = firstName + "." + lastName;
        String temporaryUserName = userName;
        int serialNumberForUserName = 0;

        while(userRepository.isExistsByUserName(temporaryUserName)) {
            temporaryUserName = userName + serialNumberForUserName++;
        }

        return temporaryUserName;
    }

    private boolean isFirstAndLastNamesNullOrBlank(String firstName, String lastName) {
        return firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank();
    }
}
