package ru.artem.NauJava.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "RegisterResponse")
public class RegisterResponse {

    @Schema(example = "true")
    private boolean success;

    @Schema(example = "User registered successfully")
    private String message;

    @Schema(example = "/login?registered=true")
    private String redirectUrl;

    @Schema(example = "User with this email already exists", nullable = true)
    private String error;
}
