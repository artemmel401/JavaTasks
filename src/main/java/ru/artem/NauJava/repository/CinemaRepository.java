package ru.artem.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.artem.NauJava.entity.Cinema;

@RepositoryRestResource(exported = false)
public interface CinemaRepository extends CrudRepository<Cinema, Long> {
}
