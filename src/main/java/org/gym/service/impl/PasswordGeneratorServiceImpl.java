package org.gym.service.impl;

import org.gym.service.PasswordGeneratorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.stream.IntStream;

@Component
public class PasswordGeneratorServiceImpl implements PasswordGeneratorService {

    private final SecureRandom secureRandom;
    private final int passwordLength;
    private final int passwordCharStartsWith;
    private final int passwordCharEndWithExclusion;

    public PasswordGeneratorServiceImpl(SecureRandom secureRandom,
                                        @Value("${password.length}") int passwordLength,
                                        @Value("${password.range.start}") int passwordCharStartsWith,
                                        @Value("${password.range.endExclusive}") int passwordCharEndWithExclusion) {
        this.secureRandom = secureRandom;
        this.passwordLength = passwordLength;
        this.passwordCharStartsWith = passwordCharStartsWith;
        this.passwordCharEndWithExclusion = passwordCharEndWithExclusion;
    }

    public String generate() {
        IntStream is = secureRandom.ints(passwordLength, passwordCharStartsWith, passwordCharEndWithExclusion);
        return is.collect(
                StringBuilder::new,
                (sb, i) -> sb.append((char)i),
                StringBuilder::append
        ).toString();
    }
}
