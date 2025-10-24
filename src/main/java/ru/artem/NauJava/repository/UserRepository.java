package ru.artem.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.artem.NauJava.entity.User;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByIsActive(Boolean isActive);
}
