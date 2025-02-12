package org.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Value;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 4, message = "User's first name must be at least 4 letters long")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "User's first name must consist of letters only (the first one is capital)")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 4, message = "User's last name must be at least 4 letters long")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "User's last name must consist of letters only (the first one is capital)")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username")
    private String userName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;
}
