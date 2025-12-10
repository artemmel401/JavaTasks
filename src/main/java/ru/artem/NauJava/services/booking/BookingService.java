package ru.artem.NauJava.services.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artem.NauJava.entity.Booking;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.model.PaymentStatus;
import ru.artem.NauJava.repository.BookingRepository;
import ru.artem.NauJava.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
public class BookingService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingRepository bookingRepository;
    public void payBooking(Long bookingId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Проверка прав
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to pay for this booking");
        }

        // Проверка статуса
        if (booking.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Cannot pay booking with status: " + booking.getPaymentStatus());
        }

        // Проверка времени
        if (booking.getSession().getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot pay for past session");
        }

        // Обновление статуса
        booking.setPaymentStatus(PaymentStatus.PAYED);

        bookingRepository.save(booking);
    }
}
