package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.model.Role;
import ru.artem.NauJava.repository.BookingRepository;
import ru.artem.NauJava.repository.UserRepository;
import ru.artem.NauJava.services.user.UserServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "admin-entity-controller")
public class AdminController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return ResponseEntity.ok((List<User>) users);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Пользователь с ID " + id + " не найден"
        ));
        return ResponseEntity.ok(user);
    }
    @PatchMapping("user/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id,
                                         @RequestBody Map<String, Object> updates
    ) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Пользователь с ID " + id + " не найден"
        ));
        updates.forEach((key, value) -> {
            switch (key) {
                case "email":
                    user.setEmail((String) value);
                    break;
                case "firstName":
                    user.setFirstName((String) value);
                    break;
                case "lastName":
                    user.setLastName((String) value);
                    break;
                case "isActive":
                    user.setActive((boolean) value);
                case "role":
                    if (value instanceof Role) {
                        user.setRole((Role) value);
                    }
                    break;
                case "password":
                    user.setPassword(passwordEncoder.encode((String) value));
                case "id":
                    break;
            }
        });

        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("user/{id}")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userServiceImpl.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
