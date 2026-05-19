package ru.artem.NauJava.dto.cinema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "UpdateCinemaRequest")
public class UpdateCinemaRequest {

    @Schema(example = "Киноплекс")
    private String name;

    @Schema(example = "ул. Ленина, 1")
    private String address;

    @Schema(example = "Иванов И.И.")
    private String director;
}
