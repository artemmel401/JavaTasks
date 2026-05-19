package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dto.booking.CreateBookingRequest;
import ru.artem.NauJava.dto.booking.PayBookingResponse;
import ru.artem.NauJava.entity.Booking;
import ru.artem.NauJava.entity.Session;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.model.PaymentStatus;
import ru.artem.NauJava.model.Role;
import ru.artem.NauJava.repository.BookingRepository;
import ru.artem.NauJava.repository.SessionRepository;
import ru.artem.NauJava.repository.UserRepository;
import ru.artem.NauJava.services.booking.BookingService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController("BookingController")
@RequestMapping("/api")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/booking")
    @Transactional
    @Tag(name = "booking-entity-controller")
    public ResponseEntity<Booking> createBooking(@RequestBody CreateBookingRequest request, Principal principal) {
        Session session = sessionRepository.findById(request.getSession_id()).orElseThrow(() -> new RuntimeException(
                "Сеанс с ID " + request.getSession_id() + " не найден"
        ));
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException(
                "Пользователь с email " + username + " не найден"
        ));
            Booking booking = new Booking();
            booking.setBookingTime(LocalDateTime.now());
            booking.setPaymentStatus(PaymentStatus.PAYED);
            booking.setTotalAmount(request.getTotal_amount());
            booking.setSession(session);
            booking.setUser(user);
            Booking saved = bookingRepository.save(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/admin/booking/findByUser")
    @Tag(name = "admin-entity-controller")
    public List<Booking> findBookingByUserId(
            @RequestParam(value = "user_id", required = false) Long userIdSnake,
            @RequestParam(value = "userId", required = false) Long userIdCamel
    ) {
        Long userId = userIdSnake != null ? userIdSnake : userIdCamel;
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        return bookingRepository.findBookingsByUserId(userId);
    }
    @GetMapping("/booking")
    @Tag(name = "booking-entity-controller")
    public ResponseEntity<List<Booking>> findMyBookings (Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException(
                "Пользователь с email " + username + " не найден"
        ));
        List<Booking> bookings = bookingRepository.findBookingsByUserId(user.getId());
        return ResponseEntity.ok(bookings);
    }
    @GetMapping("/booking/{id}")
    @Tag(name = "booking-entity-controller")
    public ResponseEntity<Booking> findBooking (Principal principal, @PathVariable Long id) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException(
                "Пользователь с email " + username + " не найден"
        ));
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Бронирование " + id + " не найдено"
        ));
        boolean isOwner = booking.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(booking);
    }
    @PostMapping("/booking/{id}/pay")
    @Tag(name = "booking-entity-controller")
    @io.swagger.v3.oas.annotations.Operation(operationId = "payBooking", summary = "payBooking")
    public ResponseEntity<PayBookingResponse> payBooking(Principal principal, @PathVariable Long id) {
        try {
            bookingService.payBooking(id, principal.getName());

            PayBookingResponse response = new PayBookingResponse();
            response.setSuccess(true);
            response.setMessage("Payment processed successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            PayBookingResponse response = new PayBookingResponse();
            response.setSuccess(false);
            response.setMessage("Error processing payment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/booking/{id}")
    @Tag(name = "booking-entity-controller")
    @ApiResponse(responseCode = "204", description = "Бронирование успешно удалено")
    @ApiResponse(responseCode = "404", description = "Бронирование не найдено")
    public ResponseEntity<Void> deleteBooking(
            @Parameter(description = "ID бронирования", required = true, example = "1")
            @PathVariable Long id) {

        if (!bookingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
