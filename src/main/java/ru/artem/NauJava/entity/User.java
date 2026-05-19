package ru.artem.NauJava.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.artem.NauJava.model.Role;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String email;

    @Column(name = "first_name")
    @JsonProperty("first_name")
    @JsonAlias("firstName")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("last_name")
    @JsonAlias("lastName")
    private String lastName;

    @Column(name = "is_active")
    @JsonProperty("is_active")
    @JsonAlias("active")
    private boolean isActive;

    @Column(name = "registration_date")
    @JsonProperty("registration_date")
    @JsonAlias("registrationDate")
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column
    private String password;

}
