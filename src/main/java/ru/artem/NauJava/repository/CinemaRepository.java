package ru.artem.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.artem.NauJava.entity.Cinema;

public interface CinemaRepository extends CrudRepository<Cinema, Long> {
}
