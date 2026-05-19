package ru.artem.NauJava.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "UpdateUserProfileRequest", description = "Обновление имени в личном кабинете")
public class UpdateUserProfileRequest {

    @Schema(example = "Иван")
    @JsonAlias("first_name")
    private String firstName;

    @Schema(example = "Иванов")
    @JsonAlias("last_name")
    private String lastName;
}
