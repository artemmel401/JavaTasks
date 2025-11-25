package ru.artem.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.artem.NauJava.entity.Booking;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.BookingRepository;
import ru.artem.NauJava.repository.UserRepository;

import java.util.List;

@SpringBootTest
class BookingTest {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    BookingTest (BookingRepository bookingRepository ,
                 UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Test
    void testFindBookingsByUserId () {
        User user = new User();
        User userSaved = userRepository.save(user);
        Booking booking = new Booking();
        booking.setUserId(user);
        bookingRepository.save(booking);
        List<Booking> foundBookings = bookingRepository.findBookingsByUserId(userSaved.getId());

        Assertions.assertNotNull(foundBookings);
        Assertions.assertEquals(1, foundBookings.size());
    }
}
