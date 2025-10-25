package ru.artem.NauJava.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.artem.NauJava.entity.Seat;

@RepositoryRestResource(path = "seats")
public interface SeatRepository  extends CrudRepository<Seat, Long> {

    @RestResource(exported = false)
    @Modifying
    @Query("DELETE FROM Seat s WHERE s.hall_id.id = :hallId")
    void deleteByHallId(Long hallId);
}
