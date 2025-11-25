package ru.artem.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.artem.NauJava.entity.Movie;

@RepositoryRestResource(path = "movies")
public interface MovieRepository  extends CrudRepository<Movie, Long> {
}
