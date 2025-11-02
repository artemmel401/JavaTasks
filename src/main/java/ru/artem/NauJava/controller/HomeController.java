package ru.artem.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.UserRepository;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "index";
    }
}
