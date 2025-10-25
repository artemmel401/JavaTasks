package ru.artem.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.artem.NauJava.entity.Booking;

import java.util.List;

@RepositoryRestResource(path = "bookings")
public interface BookingRepository extends CrudRepository<Booking, Long> {
    @RestResource(exported = false)
    @Query("SELECT b FROM Booking b WHERE b.user_id.id = :userId")
    List<Booking> findBookingsByUserId(Long userId);
}
