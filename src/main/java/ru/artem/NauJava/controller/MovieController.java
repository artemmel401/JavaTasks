package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dto.movie.CreateMovieRequest;
import ru.artem.NauJava.entity.Cinema;
import ru.artem.NauJava.entity.Movie;
import ru.artem.NauJava.repository.CinemaRepository;
import ru.artem.NauJava.repository.MovieRepository;
import ru.artem.NauJava.services.movie.MovieServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private MovieServiceImpl movieService;

    @PostMapping("/admin/movie")
    @Tag(name = "admin-entity-controller")
    @Transactional
    public ResponseEntity<Movie> createMovie(@RequestBody CreateMovieRequest request) {
        Cinema cinema = cinemaRepository.findById(request.getCinema_id())
                .orElseThrow(() -> new RuntimeException(
                        "Кинотеатр с ID " + request.getCinema_id() + " не найден"
                ));
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setCinema(cinema);

        Movie saved = movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/movie")
    @Tag(name = "movie-entity-controller")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = (List<Movie>) movieRepository.findAll();
        return ResponseEntity.ok(movies);
    }
    @GetMapping("/movie/{id}")
    @Tag(name = "movie-entity-controller")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/admin/movie/{id}")
    @Tag(name = "admin-entity-controller")
    @ApiResponse(responseCode = "404", description = "Фильм не найден")
    public ResponseEntity<Movie> partialUpdateMovie(
            @Parameter(description = "ID фильма", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Optional<Movie> existingMovieOpt = movieRepository.findById(id);
        if (existingMovieOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Movie existingMovie = existingMovieOpt.get();

        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    existingMovie.setTitle((String) value);
                    break;
                case "description":
                    existingMovie.setDescription((String) value);
                    break;
                case "durationMinutes":
                    if (value instanceof Integer) {
                        existingMovie.setDurationMinutes((Integer) value);
                    }
                    break;
                case "posterUrl":
                    existingMovie.setPosterUrl((String) value);
                    break;
                case "id":
                    break;
            }
        });

        Movie updatedMovie = movieRepository.save(existingMovie);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/admin/movie/{id}")
    @Tag(name = "admin-entity-controller")
    @ApiResponse(responseCode = "204", description = "Фильм успешно удален")
    @ApiResponse(responseCode = "404", description = "Фильм не найден")
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "ID фильма", required = true, example = "1")
            @PathVariable Long id) {

        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }
}