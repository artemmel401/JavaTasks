package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.artem.NauJava.util.QueryParamUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dto.hall.CreateHallRequest;
import ru.artem.NauJava.dto.hall.UpdateHallRequest;
import ru.artem.NauJava.entity.Cinema;
import ru.artem.NauJava.entity.Hall;
import ru.artem.NauJava.repository.CinemaRepository;
import ru.artem.NauJava.repository.HallRepository;
import ru.artem.NauJava.services.hall.HallServiceImpl;

import java.util.List;
import java.util.Optional;

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
    @Operation(operationId = "getHalls", summary = "getHalls")
    public List<Hall> getHalls(
            @Parameter(name = "cinema_id", description = "ID кинотеатра — вернуть только залы этого кинотеатра")
            @RequestParam(value = "cinema_id", required = false) Long cinemaIdSnake,
            @Parameter(name = "cinemaId", description = "ID кинотеатра (camelCase)")
            @RequestParam(value = "cinemaId", required = false) Long cinemaIdCamel
    ) {
        Long cinemaId = QueryParamUtils.resolveId(cinemaIdSnake, cinemaIdCamel);
        if (cinemaId == null) {
            return (List<Hall>) hallRepository.findAll();
        }
        return hallRepository.findByCinemaId(cinemaId);
    }
    @PostMapping("/admin/hall")
    @Tag(name = "admin-entity-controller")
    public ResponseEntity<Hall> createHall(@RequestBody CreateHallRequest request) {
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
    }

    @PatchMapping("/admin/hall/{id}")
    @Tag(name = "admin-entity-controller")
    @Operation(operationId = "updateHall", summary = "updateHall")
    public ResponseEntity<Hall> updateHall(
            @PathVariable Long id,
            @RequestBody UpdateHallRequest request
    ) {
        Optional<Hall> optHall = hallRepository.findById(id);
        if (optHall.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Hall hall = optHall.get();
        if (request.getName() != null) {
            hall.setName(request.getName());
        }
        if (request.getSeatsPerRow() != null) {
            hall.setSeatsPerRow(request.getSeatsPerRow());
        }
        if (request.getTotalRows() != null) {
            hall.setTotalRows(request.getTotalRows());
        }
        if (request.getCinemaId() != null) {
            Cinema cinema = cinemaRepository.findById(request.getCinemaId()).orElseThrow(() -> new RuntimeException(
                    "Кинотеатр с ID " + request.getCinemaId() + " не найден"
            ));
            hall.setCinema(cinema);
        }

        return ResponseEntity.ok(hallRepository.save(hall));
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
