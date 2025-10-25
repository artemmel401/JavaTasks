package ru.artem.NauJava.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.artem.NauJava.dao.UserRepositoryCustom;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryCustom userRepositoryCustom;

    @GetMapping("/findByIsActive")
    public List<User> findByIsActive(@RequestParam boolean isActive)
    {
        return userRepository.findByIsActive(isActive);
    }

    @GetMapping("/findByEmail")
    public List<User> findByEmail(@RequestParam String email) {
        return userRepositoryCustom.findByEmail(email);
    }
    @GetMapping("/findByDate")
    public List<User> findByRegistrationDate(@RequestParam LocalDateTime date) {
        return userRepositoryCustom.findByRegistrationDate(date);
    }
}
