package ru.artem.NauJava.controller.view;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.artem.NauJava.entity.Cinema;
import ru.artem.NauJava.entity.Movie;
import ru.artem.NauJava.entity.Session;
import ru.artem.NauJava.repository.CinemaRepository;
import ru.artem.NauJava.repository.MovieRepository;
import ru.artem.NauJava.repository.SessionRepository;

import java.security.Principal;
import java.util.Optional;

@Controller
public class BookingControllerView {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping(path = "/cinemas/{cinemaId}/{movieId}/{sessionId}/booking")
    public String showBookingPage(
            @PathVariable Long cinemaId,
            @PathVariable Long movieId,
            @PathVariable Long sessionId,
            Model model, Principal principal) {

        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);
        Optional<Movie> movieOpt = movieRepository.findById(movieId);
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);

        if (cinemaOpt.isEmpty() || movieOpt.isEmpty() || sessionOpt.isEmpty()) {
            System.out.println("ERROR: Some entities not found!");
            return "redirect:/";
        }
        if (principal == null) {
            return "redirect:/login";
        }

        Cinema cinema = cinemaOpt.get();
        Movie movie = movieOpt.get();
        Session session = sessionOpt.get();

        model.addAttribute("cinema", cinema);
        model.addAttribute("movie", movie);
        model.addAttribute("movieSession", session);
        return "booking";
    }
}
