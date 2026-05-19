package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dto.cinema.CreateCinemaRequest;
import ru.artem.NauJava.dto.cinema.UpdateCinemaRequest;
import ru.artem.NauJava.entity.Cinema;
import ru.artem.NauJava.repository.CinemaRepository;
import ru.artem.NauJava.services.cinema.CinemaServiceImpl;

import java.util.List;
import java.util.Optional;


@RestController("CinemaController")
@RequestMapping("/api")
public class CinemaController {

    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private CinemaServiceImpl cinemaService;

    @PostMapping("/admin/cinema")
    @Tag(name = "admin-entity-controller")
    public ResponseEntity<Cinema> createCinema(@RequestBody CreateCinemaRequest request) {
        Cinema cinema = new Cinema();
        cinema.setAddress(request.getAddress());
        cinema.setDirector(request.getDirector());
        cinema.setName(request.getName());

        Cinema saved = cinemaRepository.save(cinema);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/cinema")
    @Tag(name = "cinema-entity-controller")
    public ResponseEntity<List<Cinema>> getAllCinemas() {
        List<Cinema> cinemas = (List<Cinema>) cinemaRepository.findAll();
        return ResponseEntity.ok(cinemas);
    }
    @GetMapping("/cinema/{id}")
    @Tag(name = "cinema-entity-controller")
    public ResponseEntity<Cinema> getCinemaById(@PathVariable Long id) {
        return cinemaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/cinema/{id}")
    @Tag(name = "admin-entity-controller")
    public ResponseEntity<Void> deleteCinemaById(@PathVariable Long id) {
        cinemaService.deleteCinema(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/cinema/{id}")
    @Tag(name = "admin-entity-controller")
    @io.swagger.v3.oas.annotations.Operation(operationId = "updateCinema", summary = "updateCinema")
    @ApiResponse(responseCode = "404", description = "Кинотеатр не найден")
    public ResponseEntity<Cinema> updateCinema(
            @Parameter(description = "ID кинотеатра", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody UpdateCinemaRequest request
    ) {
        Optional<Cinema> existingCinemaOpt = cinemaRepository.findById(id);
        if (existingCinemaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cinema cinema = existingCinemaOpt.get();
        if (request.getAddress() != null) {
            cinema.setAddress(request.getAddress());
        }
        if (request.getDirector() != null) {
            cinema.setDirector(request.getDirector());
        }
        if (request.getName() != null) {
            cinema.setName(request.getName());
        }

        return ResponseEntity.ok(cinemaRepository.save(cinema));
    }
}
