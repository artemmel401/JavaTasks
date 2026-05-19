package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artem.NauJava.dao.UserRepositoryCustom;
import ru.artem.NauJava.dto.user.UpdateUserProfileRequest;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.UserRepository;
import ru.artem.NauJava.util.QueryParamUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
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
    @Operation(operationId = "getUserProfile", summary = "getUserProfile")
    public ResponseEntity<User> getUserProfile(Principal principal) {
        String email = principal.getName();
        Optional<User> optUser = userRepository.findByEmail(email);
        return optUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/user/findByIsActive")
    @Tag(name = "admin-entity-controller")
    @Operation(operationId = "findUsersByIsActive", summary = "findUsersByIsActive")
    public List<User> findByIsActive(
            @Parameter(name = "is_active", description = "Фильтр по активности")
            @RequestParam(value = "is_active", required = false) Boolean isActiveSnake,
            @Parameter(name = "isActive", description = "Фильтр по активности (camelCase)")
            @RequestParam(value = "isActive", required = false) Boolean isActiveCamel
    ) {
        Boolean isActive = isActiveSnake != null ? isActiveSnake : isActiveCamel;
        if (isActive == null) {
            throw new IllegalArgumentException("is_active or isActive is required");
        }
        return userRepository.findByIsActive(isActive);
    }

    @GetMapping("/admin/user/findByEmail")
    @Tag(name = "admin-entity-controller")
    @Operation(operationId = "findUserByEmail", summary = "findUserByEmail")
    public Optional<User> findByEmail(
            @Parameter(description = "Email пользователя", required = true)
            @RequestParam String email
    ) {
        return userRepositoryCustom.findByEmail(email);
    }

    @GetMapping("/admin/user/findByDate")
    @Tag(name = "admin-entity-controller")
    @Operation(operationId = "findUsersByRegistrationDate", summary = "findUsersByRegistrationDate")
    public List<User> findByRegistrationDate(
            @Parameter(description = "Дата регистрации", required = true)
            @RequestParam LocalDateTime date
    ) {
        return userRepositoryCustom.findByRegistrationDate(date);
    }

    @PatchMapping("/user/{id}")
    @Tag(name = "user-entity-controller")
    @Operation(operationId = "updateUserProfile", summary = "updateUserProfile")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "403", description = "Можно изменять только свой профиль")
    public ResponseEntity<User> updateUserProfile(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody UpdateUserProfileRequest request,
            Principal principal
    ) {
        User currentUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if (currentUser == null || !currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optUser.get();
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        return ResponseEntity.ok(userRepository.save(user));
    }
}
