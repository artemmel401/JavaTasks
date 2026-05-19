package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.artem.NauJava.util.QueryParamUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dto.session.CreateSessionRequest;
import ru.artem.NauJava.dto.session.UpdateSessionRequest;
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

import java.util.List;
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
    @Operation(operationId = "getAllSessions", summary = "getAllSessions")
    public ResponseEntity<List<Session>> getAllSessions(
            @Parameter(name = "movie_id", description = "ID фильма")
            @RequestParam(value = "movie_id", required = false) Long movieIdSnake,
            @Parameter(name = "movieId", description = "ID фильма (camelCase)")
            @RequestParam(value = "movieId", required = false) Long movieIdCamel,
            @Parameter(name = "hall_id", description = "ID зала")
            @RequestParam(value = "hall_id", required = false) Long hallIdSnake,
            @Parameter(name = "hallId", description = "ID зала (camelCase)")
            @RequestParam(value = "hallId", required = false) Long hallIdCamel,
            @Parameter(name = "cinema_id", description = "ID кинотеатра (через зал)")
            @RequestParam(value = "cinema_id", required = false) Long cinemaIdSnake,
            @Parameter(name = "cinemaId", description = "ID кинотеатра (camelCase)")
            @RequestParam(value = "cinemaId", required = false) Long cinemaIdCamel
    ) {
        Long movieId = QueryParamUtils.resolveId(movieIdSnake, movieIdCamel);
        Long hallId = QueryParamUtils.resolveId(hallIdSnake, hallIdCamel);
        Long cinemaId = QueryParamUtils.resolveId(cinemaIdSnake, cinemaIdCamel);

        List<Session> sessions;
        if (movieId != null && hallId != null) {
            sessions = sessionRepository.findByHallAndMovieId(hallId, movieId);
        } else if (movieId != null) {
            sessions = sessionRepository.findByMovieId(movieId);
        } else if (hallId != null) {
            sessions = sessionRepository.findByHallId(hallId);
        } else if (cinemaId != null) {
            sessions = sessionRepository.findByCinemaId(cinemaId);
        } else {
            sessions = (List<Session>) sessionRepository.findAll();
        }
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
    @Operation(operationId = "updateSession", summary = "updateSession")
    public ResponseEntity<Session> updateSession(
            @PathVariable Long id,
            @RequestBody UpdateSessionRequest request
    ) {
        Optional<Session> optSession = sessionRepository.findById(id);
        if (optSession.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Session session = optSession.get();

        if (request.getStartTime() != null) {
            session.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            session.setEndTime(request.getEndTime());
        }
        if (request.getPrice() != null) {
            session.setPrice(request.getPrice());
        }
        if (request.getHallId() != null) {
            Hall hall = hallRepository.findById(request.getHallId()).orElseThrow(() -> new RuntimeException(
                    "Зал с ID " + request.getHallId() + " не найден"
            ));
            session.setHall(hall);
        }
        if (request.getMovieId() != null) {
            Movie movie = movieRepository.findById(request.getMovieId()).orElseThrow(() -> new RuntimeException(
                    "Фильм с ID " + request.getMovieId() + " не найден"
            ));
            session.setMovie(movie);
        }

        return ResponseEntity.ok(sessionRepository.save(session));
    }
}
