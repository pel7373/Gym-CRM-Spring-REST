package org.gym.facade.impl;

import org.springframework.stereotype.Component;

@Component
public class UserNameAndPasswordChecker {
    public boolean isNullOrBlank(String userName, String password) {
        return isNullOrBlank(userName) || isNullOrBlank(password);
    }

    public boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }
}
