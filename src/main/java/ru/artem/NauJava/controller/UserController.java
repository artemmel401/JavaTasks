package ru.artem.NauJava.controller;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dao.UserRepositoryCustom;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryCustom userRepositoryCustom;

    @GetMapping("/user/profile")
    @Tag(name = "user-entity-controller")
    public ResponseEntity<User> getUser(Principal principal) {
        String email = principal.getName();
        Optional<User> optUser = userRepository.findByEmail(email);
        return optUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/user/findByIsActive")
    @Tag(name = "admin-entity-controller")
    public List<User> findByIsActive(@RequestParam boolean isActive)
    {
        return userRepository.findByIsActive(isActive);
    }

    @GetMapping("/admin/user/findByEmail")
    @Tag(name = "admin-entity-controller")
    public Optional<User> findByEmail(@RequestParam String email) {
        return userRepositoryCustom.findByEmail(email);
    }
    @GetMapping("/admin/user/findByDate")
    @Tag(name = "admin-entity-controller")
    public List<User> findByRegistrationDate(@RequestParam LocalDateTime date) {
        return userRepositoryCustom.findByRegistrationDate(date);
    }

    @PatchMapping("/user/{id}")
    @Tag(name = "user-entity-controller")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<User> editUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optUser.get();
        updates.forEach((key, value) -> {
            switch (key) {
                case "firstName":
                    user.setFirstName((String) value);
                    break;
                case "lastName":
                    user.setLastName((String) value);
                    break;
                case "id":
                    break;
            }
        });
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }
}
