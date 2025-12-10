package ru.artem.NauJava.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.artem.NauJava.entity.Movie;
import ru.artem.NauJava.repository.MovieRepository;

import java.security.Principal;

@Controller
public class MovieControllerView {
    @Autowired
    MovieRepository movieRepository;
    @GetMapping(path = "/cinemas/{cinemaId}")
    public String movieListView(@PathVariable Long cinemaId, Model model, Principal principal)
    {
        Iterable<Movie> movies = movieRepository.findByCinemaId(cinemaId);
        model.addAttribute("movies", movies);
        model.addAttribute("cinemaId", cinemaId);
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("isAuthenticated", true);
        } else {
            model.addAttribute("isAuthenticated", false);
        }
        return "movies";
    }
}
