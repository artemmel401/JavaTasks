package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import ru.artem.NauJava.dto.auth.LoginErrorResponse;
import ru.artem.NauJava.dto.auth.LoginRequest;
import ru.artem.NauJava.dto.auth.LoginResponse;
import ru.artem.NauJava.dto.auth.RegisterResponse;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.services.user.UserDetailService;

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
    @Operation(operationId = "registerUser", summary = "registerUser")
    @ApiResponse(responseCode = "200", description = "Успешная регистрация",
            content = @Content(schema = @Schema(implementation = RegisterResponse.class)))
    @ApiResponse(responseCode = "400", description = "Email уже занят",
            content = @Content(schema = @Schema(implementation = RegisterResponse.class)))
    public ResponseEntity<RegisterResponse> addUser(@RequestBody User user) {
        RegisterResponse response = new RegisterResponse();

        try {
            userService.addUser(user);

            response.setSuccess(true);
            response.setMessage("User registered successfully");
            response.setRedirectUrl("/login?registered=true");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage("User with this email already exists");
            response.setError(ex.getMessage());

            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage("Registration failed");
            response.setError(ex.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    @Operation(operationId = "login", summary = "login")
    @ApiResponse(responseCode = "200", description = "Успешный вход",
            content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", description = "Неверные учётные данные",
            content = @Content(schema = @Schema(implementation = LoginErrorResponse.class)))
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String token = jwtTokenProvider.generateToken(auth);

        LoginResponse response = new LoginResponse();
        response.setSuccess(true);
        response.setToken(token);
        response.setMessage("Login successful");

        return ResponseEntity.ok(response);
    }
}
