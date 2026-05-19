package ru.artem.NauJava.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "LoginResponse")
public class LoginResponse {

    @Schema(example = "true")
    private boolean success;

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(example = "Login successful")
    private String message;
}
