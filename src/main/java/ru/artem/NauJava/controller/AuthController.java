package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.config.JwtTokenProvider;
import ru.artem.NauJava.dto.auth.LoginRequest;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.services.user.UserDetailService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth-entity-controller")
public class AuthController {

    @Autowired
    private UserDetailService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        try {
            userService.addUser(user);

            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("redirectUrl", "/login?registered=true");

            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException ex) {
            response.put("success", false);
            response.put("message", "User with this email already exists");
            response.put("error", ex.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
        catch (Exception ex) {
            response.put("success", false);
            response.put("message", "Registration failed");
            response.put("error", ex.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Генерируем токен
            String token = jwtTokenProvider.generateToken(auth);

            // Возвращаем JSON
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.print(e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid email or password");
            return ResponseEntity.status(401).body(error);
        }
    }
}
