package ru.artem.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.UserRepository;

@Controller
@RequestMapping("users/view")
public class UserControllerView
{
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/list")
    public String userListView(Model model)
    {
        Iterable<User> products = userRepository.findAll();
        model.addAttribute("users", products);
        return "index";
    }
}

