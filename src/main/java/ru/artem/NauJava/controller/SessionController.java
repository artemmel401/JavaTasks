package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dto.session.CreateSessionRequest;
import ru.artem.NauJava.entity.Booking;
import ru.artem.NauJava.entity.Hall;
import ru.artem.NauJava.entity.Movie;
import ru.artem.NauJava.entity.Session;
import ru.artem.NauJava.model.PaymentStatus;
import ru.artem.NauJava.repository.BookingRepository;
import ru.artem.NauJava.repository.HallRepository;
import ru.artem.NauJava.repository.MovieRepository;
import ru.artem.NauJava.repository.SessionRepository;
import ru.artem.NauJava.services.session.SessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SessionController {

    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/admin/session")
    @Transactional
    @Tag(name = "admin-entity-controller")
    public ResponseEntity<Session> createSession(@RequestBody CreateSessionRequest request) {
        Movie movie = movieRepository.findById(request.getMovie_id())
                .orElseThrow(() -> new RuntimeException(
                        "Фильм с ID " + request.getMovie_id() + " не найден"
                ));
        Hall hall = hallRepository.findById(request.getHall_id()) .orElseThrow(() -> new RuntimeException(
                "Зал с ID " + request.getHall_id() + " не найден"
        ));
        Session session = new Session();
        session.setStartTime(request.getStart_time());
        session.setEndTime(request.getEnd_time());
        session.setPrice(request.getPrice());
        session.setHall(hall);
        session.setMovie(movie);
        Session saved = sessionRepository.save(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @GetMapping("/session")
    @Tag(name = "session-entity-controller")
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = (List<Session>) sessionRepository.findAll();
        return ResponseEntity.ok(sessions);
    }
    @GetMapping("/session/{id}")
    @Tag(name = "session-entity-controller")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return sessionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/session/{id}/seats")
    @Tag(name = "session-entity-controller")
    public ResponseEntity<Integer> getFreeSeatsCount(@PathVariable Long id) {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        List<Booking> bookings = bookingRepository.findBookingsSessionId(id);
        List<Booking> filteredBookings = bookings.stream()
                .filter(b -> b.getPaymentStatus().equals(PaymentStatus.PAYED) ||
                        b.getPaymentStatus().equals(PaymentStatus.PENDING)).toList();
        if (optionalSession.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Session session = optionalSession.get();
        int totalSeats = session.getHall().getSeatsPerRow() * session.getHall().getTotalRows();
        return ResponseEntity.ok(totalSeats - filteredBookings.size());
    }

    @DeleteMapping("/admin/session/{id}")
    @Tag(name = "admin-entity-controller")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/session/{id}")
    @Tag(name = "admin-entity-controller")
    public ResponseEntity<Session> editSession(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Session> optSession = sessionRepository.findById(id);
        if (optSession.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Session session = optSession.get();

        updates.forEach((key, value) -> {
            switch (key) {
                case "startTime":
                    LocalDateTime startDateTime = LocalDateTime.parse((String) value);
                    session.setStartTime(startDateTime);
                    break;
                case "endTime":
                    LocalDateTime endDateTime = LocalDateTime.parse((String) value);
                    session.setEndTime(endDateTime);
                    break;
                case "price":
                    if (value instanceof Integer) {
                        session.setPrice((Integer) value);
                    }
                    break;
                case "hallId":
                    Hall hall = hallRepository.findById(((Number) value).longValue()).orElseThrow(() -> new RuntimeException(
                            "Зал с ID " + value + " не найден"
                    ));
                    session.setHall(hall);
                    break;
                case "id":
                    break;
            }
        });

        Session updatedSession = sessionRepository.save(session);
        return ResponseEntity.ok(updatedSession);
    }
}
