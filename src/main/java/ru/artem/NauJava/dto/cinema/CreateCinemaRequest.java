package ru.artem.NauJava.dto.cinema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCinemaRequest {
    @Schema(example = "Address")
    private String address;

    @Schema(example = "Director")
    private String director;

    @Schema(example = "name")
    private String name;
}
