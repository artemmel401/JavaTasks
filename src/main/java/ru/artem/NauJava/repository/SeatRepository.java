package ru.artem.NauJava.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.artem.NauJava.entity.Seat;

public interface SeatRepository  extends CrudRepository<Seat, Long> {

    @Modifying
    @Query("DELETE FROM Seat s WHERE s.hall_id.id = :hallId")
    void deleteByHallId(Long hallId);
}
