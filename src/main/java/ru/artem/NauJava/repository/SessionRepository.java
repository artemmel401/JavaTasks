package ru.artem.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.artem.NauJava.entity.Session;

@RepositoryRestResource(path = "sessions")
public interface SessionRepository extends CrudRepository<Session, Long> {
}
