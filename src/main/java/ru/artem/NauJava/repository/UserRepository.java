package ru.artem.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.artem.NauJava.entity.User;
import java.util.List;

@RepositoryRestResource(path = "users")
public interface UserRepository extends CrudRepository<User, Long> {
    @RestResource(exported = false)
    List<User> findByIsActive(Boolean isActive);
}
