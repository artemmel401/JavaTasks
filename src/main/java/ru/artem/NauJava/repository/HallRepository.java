package ru.artem.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.artem.NauJava.entity.Booking;
import ru.artem.NauJava.entity.Hall;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface HallRepository  extends CrudRepository<Hall, Long> {
    @RestResource(exported = false)
    @Query("SELECT h FROM Hall h WHERE h.cinema.id = :cinemaId")
    List<Hall> findByCinemaId(Long cinemaId);
}
