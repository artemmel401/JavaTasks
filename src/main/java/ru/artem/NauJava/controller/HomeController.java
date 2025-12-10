package ru.artem.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.artem.NauJava.entity.Cinema;
import ru.artem.NauJava.entity.Movie;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.CinemaRepository;
import ru.artem.NauJava.repository.MovieRepository;
import ru.artem.NauJava.repository.UserRepository;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CinemaRepository cinemaRepository;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        Iterable<Cinema> cinemas = cinemaRepository.findAll();
        model.addAttribute("cinemas", cinemas);
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("isAuthenticated", true);
        } else {
            model.addAttribute("isAuthenticated", false);
        }
        return "index";
    }
}
