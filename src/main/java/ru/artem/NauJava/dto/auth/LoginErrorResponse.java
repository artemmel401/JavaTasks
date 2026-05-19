package ru.artem.NauJava.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginErrorResponse")
public class LoginErrorResponse {

    @Schema(example = "Invalid email or password")
    private String error;
}
