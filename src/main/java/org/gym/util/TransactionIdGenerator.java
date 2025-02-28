package org.gym.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionIdGenerator {
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
