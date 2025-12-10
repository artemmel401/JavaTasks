package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dto.hall.CreateHallRequest;
import ru.artem.NauJava.entity.Cinema;
import ru.artem.NauJava.entity.Hall;
import ru.artem.NauJava.repository.CinemaRepository;
import ru.artem.NauJava.repository.HallRepository;
import ru.artem.NauJava.services.hall.HallServiceImpl;

import java.util.List;
import java.util.Map;

@RestController("HallController")
@RequestMapping("/api")
public class HallController {

    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private HallServiceImpl hallServiceImpl;

    @GetMapping("/hall")
    @Tag(name = "hall-entity-controller")
    public List<Hall> getHalls(@RequestParam(required = false) Long cinemaId) {
        if (cinemaId == null) {
            return (List<Hall>) hallRepository.findAll();
        } else {
            return hallRepository.findByCinemaId(cinemaId);
        }
    }
    @PostMapping("/admin/hall")
    @Tag(name = "admin-entity-controller")
    public ResponseEntity<?> createHall(@RequestBody CreateHallRequest request) {
        try {
            Cinema cinema = cinemaRepository.findById(request.getCinema_id())
                    .orElseThrow(() -> new RuntimeException(
                            "Кинотеатр с ID " + request.getCinema_id() + " не найден"
                    ));

            Hall hall = new Hall();
            hall.setName(request.getName());
            hall.setSeatsPerRow(request.getSeatsPerRow());
            hall.setTotalRows(request.getTotalRows());
            hall.setCinema(cinema);

            Hall saved = hallRepository.save(hall);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/admin/hall/{id}")
    @Tag(name = "admin-entity-controller")
    @ApiResponse(responseCode = "204", description = "Зал успешно удален")
    @ApiResponse(responseCode = "404", description = "Зал не найден")
    public ResponseEntity<Void> deleteHall(
            @Parameter(description = "ID зала", required = true, example = "1")
            @PathVariable Long id) {

        if (!hallRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        hallServiceImpl.deleteHall(id);
        return ResponseEntity.ok().build();
    }
}
