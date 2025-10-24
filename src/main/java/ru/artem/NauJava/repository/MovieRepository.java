package ru.artem.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.artem.NauJava.entity.Movie;

public interface MovieRepository  extends CrudRepository<Movie, Long> {
}
