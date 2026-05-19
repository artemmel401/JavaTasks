package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dto.user.AdminUpdateUserRequest;
import ru.artem.NauJava.entity.Booking;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.BookingRepository;
import ru.artem.NauJava.repository.UserRepository;
import ru.artem.NauJava.services.user.UserServiceImpl;

import java.util.List;

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
    @Operation(operationId = "getAllUsers", summary = "getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return ResponseEntity.ok((List<User>) users);
    }

    @GetMapping("/user/{id}")
    @Operation(operationId = "getAdminUserById", summary = "getAdminUserById")
    public ResponseEntity<User> getAdminUserById(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id
    ) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Пользователь с ID " + id + " не найден"
        ));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/booking")
    @Operation(operationId = "getAllBookings", summary = "getAllBookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok((List<Booking>) bookingRepository.findAll());
    }

    @PatchMapping("/user/{id}")
    @Operation(operationId = "updateAdminUser", summary = "updateAdminUser")
    public ResponseEntity<User> updateAdminUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody AdminUpdateUserRequest request
    ) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Пользователь с ID " + id + " не найден"
        ));

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/user/{id}")
    @Operation(operationId = "deleteAdminUser", summary = "deleteAdminUser")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<Void> deleteAdminUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userServiceImpl.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
