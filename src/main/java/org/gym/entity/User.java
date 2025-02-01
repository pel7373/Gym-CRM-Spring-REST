package org.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @NotNull
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
