package ru.artem.NauJava.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.model.Role;
import ru.artem.NauJava.repository.*;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AdminControllerView {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    HallRepository hallRepository;

    @GetMapping("/admin")
    public String redirectPage() {return "redirect:/admin/users";}
    @GetMapping("/admin/users")
    public String adminUsersPage(Model model, Principal principal) {
        String email = principal.getName();
        Optional<User> optUser = userRepository.findByEmail(email);
        if (email == null || email.isEmpty() || optUser.isEmpty()) {
            return "redirect:/login";
        }
        User user = optUser.get();
        if (user.getRole() == Role.ADMIN) {
            model.addAttribute("users", userRepository.findAll());
            return "adminUser";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/bookings")
    public String adminBookingsPage(Model model, Principal principal) {
        String email = principal.getName();
        Optional<User> optUser = userRepository.findByEmail(email);
        if (email == null || email.isEmpty() || optUser.isEmpty()) {
            return "redirect:/login";
        }
        User user = optUser.get();
        if (user.getRole() == Role.ADMIN) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("bookings", bookingRepository.findAll());
            return "adminBooking";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/cinemas")
    public String adminCinemasPage(Model model, Principal principal) {
        String email = principal.getName();
        Optional<User> optUser = userRepository.findByEmail(email);
        if (email == null || email.isEmpty() || optUser.isEmpty()) {
            return "redirect:/login";
        }
        User user = optUser.get();
        if (user.getRole() == Role.ADMIN) {
            model.addAttribute("cinemas", cinemaRepository.findAll());
            return "adminCinema";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/movies")
    public String adminMoviesPage(Model model, Principal principal) {
        String email = principal.getName();
        Optional<User> optUser = userRepository.findByEmail(email);
        if (email == null || email.isEmpty() || optUser.isEmpty()) {
            return "redirect:/login";
        }
        User user = optUser.get();
        if (user.getRole() == Role.ADMIN) {
            model.addAttribute("movies", movieRepository.findAll());
            return "adminMovie";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/sessions")
    public String adminSessionsPage(Model model, Principal principal) {
        String email = principal.getName();
        Optional<User> optUser = userRepository.findByEmail(email);
        if (email == null || email.isEmpty() || optUser.isEmpty()) {
            return "redirect:/login";
        }
        User user = optUser.get();
        if (user.getRole() == Role.ADMIN) {
            model.addAttribute("movieSessions", sessionRepository.findAll());
            return "adminSession";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/halls")
    public String adminHallsPage(Model model, Principal principal) {
        String email = principal.getName();
        Optional<User> optUser = userRepository.findByEmail(email);
        if (email == null || email.isEmpty() || optUser.isEmpty()) {
            return "redirect:/login";
        }
        User user = optUser.get();
        if (user.getRole() == Role.ADMIN) {
            model.addAttribute("halls", hallRepository.findAll());
            return "adminHall";
        }
        return "redirect:/login";
    }
}
