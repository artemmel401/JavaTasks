package ru.artem.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.artem.NauJava.entity.Movie;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MovieRepository extends CrudRepository<Movie, Long> {
    @RestResource(exported = false)
    @Query("SELECT m FROM Movie m WHERE m.cinema.id = :cinemaId")
    List<Movie> findByCinemaId(Long cinemaId);
}
