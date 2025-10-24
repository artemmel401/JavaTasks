package ru.artem.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.artem.NauJava.entity.Booking;

import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.user_id.id = :userId")
    List<Booking> findBookingsByUserId(Long userId);
}
