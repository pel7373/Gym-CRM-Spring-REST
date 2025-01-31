package org.gym.service;

import org.gym.exception.NullEntityException;

public interface UserNameGeneratorService {
    String generate(String firstName, String lastName) throws NullEntityException;
}
