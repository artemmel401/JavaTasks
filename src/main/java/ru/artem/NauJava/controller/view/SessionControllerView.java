package ru.artem.NauJava.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.artem.NauJava.entity.Cinema;
import ru.artem.NauJava.entity.Hall;
import ru.artem.NauJava.entity.Movie;
import ru.artem.NauJava.entity.Session;
import ru.artem.NauJava.repository.CinemaRepository;
import ru.artem.NauJava.repository.HallRepository;
import ru.artem.NauJava.repository.MovieRepository;
import ru.artem.NauJava.repository.SessionRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class SessionControllerView {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping(path = "/cinemas/{cinemaId}/{movieId}")
    public String getSessionsByMovie(
            @PathVariable Long cinemaId,
            @PathVariable Long movieId,
            Model model,
            Principal principal
    ) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);
        Optional<Movie> movieOpt = movieRepository.findById(movieId);

        if (cinemaOpt.isEmpty() || movieOpt.isEmpty()) {
            return "redirect:/cinemas";
        }

        Cinema cinema = cinemaOpt.get();
        Movie movie = movieOpt.get();

        List<Hall> halls = hallRepository.findByCinemaId(cinemaId);
        if (halls.isEmpty()) {
            model.addAttribute("error", "В этом кинотеатре нет залов");
            return "movies";
        }

        Hall hall = halls.getFirst();

        List<Session> sessions = sessionRepository.findByHallAndMovieId(hall.getId(), movie.getId());

        model.addAttribute("movieSessions", sessions);
        model.addAttribute("cinema", cinema);
        model.addAttribute("movie", movie);
        model.addAttribute("hall", hall);

        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("isAuthenticated", true);
        } else {
            model.addAttribute("isAuthenticated", false);
        }

        return "session";
    }
}
