package ru.artem.NauJava.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.artem.NauJava.model.Role;

@Getter
@Setter
@Schema(name = "AdminUpdateUserRequest", description = "Обновление пользователя администратором")
public class AdminUpdateUserRequest {

    @Schema(example = "user@example.com")
    private String email;

    @Schema(example = "Иван")
    @JsonAlias("first_name")
    private String firstName;

    @Schema(example = "Иванов")
    @JsonAlias("last_name")
    private String lastName;

    @Schema(example = "USER", allowableValues = {"USER", "ADMIN"})
    private Role role;

    @Schema(example = "true")
    @JsonAlias({"is_active", "active"})
    private Boolean active;

    @Schema(example = "newPassword123", description = "Оставьте пустым, если пароль не меняется")
    private String password;
}
