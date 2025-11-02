package ru.artem.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.artem.NauJava.entity.Booking;
import ru.artem.NauJava.repository.BookingRepository;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/findByUser")
    public List<Booking> findBookingByUserId (@RequestParam Long userId) {
        return bookingRepository.findBookingsByUserId(userId);
    }
}
