package ru.artem.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.artem.NauJava.entity.Session;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface SessionRepository extends CrudRepository<Session, Long> {
    @RestResource(exported = false)
    @Query("SELECT s FROM Session s WHERE s.movie.id = :movieId AND s.hall.id = :hallId")
    List<Session> findByHallAndMovieId(@Param("hallId") Long hallId, @Param("movieId") Long movieId);

    @RestResource(exported = false)
    @Query("SELECT s FROM Session s WHERE s.movie.id = :movieId")
    List<Session> findByMovieId(@Param("movieId") Long movieId);

    @RestResource(exported = false)
    @Query("SELECT s FROM Session s WHERE s.hall.id = :hallId")
    List<Session> findByHallId(@Param("hallId") Long hallId);


}
