package org.gym.dto;

import net.bytebuddy.asm.Advice;

import java.time.LocalDate;

public class RegisterTraineeRequest {
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String address;

    @Override
    public String toString() {
        return "RegisterTraineeRequest{}";
    }
}
