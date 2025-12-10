package ru.artem.NauJava.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.artem.NauJava.entity.User;

@RepositoryRestResource(path = "users")
public interface UserRepositoryCustom
{
    Optional<User> findByEmail(String email);

    List<User> findByRegistrationDate(LocalDateTime localDateTime);
}
