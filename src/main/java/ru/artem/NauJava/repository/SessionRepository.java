package ru.artem.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.artem.NauJava.entity.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {
}
