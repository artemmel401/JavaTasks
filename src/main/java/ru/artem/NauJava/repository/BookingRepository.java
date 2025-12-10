package ru.artem.NauJava.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.artem.NauJava.entity.Booking;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface BookingRepository extends CrudRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findBookingsByUserId(Long userId);
    @Query("SELECT b FROM Booking b WHERE b.session.id = :sessionId")
    List<Booking> findBookingsSessionId(Long sessionId);
    @RestResource(exported = false)
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.session.id = :sessionId")
    void deleteBySessionId(@Param("sessionId") Long sessionId);
    @RestResource(exported = false)
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
