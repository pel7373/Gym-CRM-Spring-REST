package org.gym.service;

import org.gym.service.impl.PasswordGeneratorServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordGeneratorServiceImplTest {

    @Mock
    SecureRandom secureRandom;

    @ParameterizedTest
    @ValueSource(ints = {10, 11, 12})
    void generate(int passwordLength) {
        MockitoAnnotations.openMocks(this);
        PasswordGeneratorServiceImpl passwordGeneratorService = new PasswordGeneratorServiceImpl(secureRandom, passwordLength, 33, 127);

        int charForPassword = 65;
        int passwordCharStartsWith = 33;
        int passwordCharEndWithExclusion = 127;

        IntStream is = IntStream.generate(() -> charForPassword).limit(passwordLength);
        when(secureRandom.ints(passwordLength, passwordCharStartsWith, passwordCharEndWithExclusion)).
                thenReturn(is);
        String passwordActual = passwordGeneratorService.generate();
        String passwordExpected = IntStream.generate(() -> charForPassword).limit(passwordLength)
                .collect(
                        StringBuilder::new,
                        (sb, i) -> sb.append((char)i),
                        StringBuilder::append
                ).toString();

        assertNotNull(passwordActual);
        assertEquals(passwordExpected, passwordActual);
    }
}
