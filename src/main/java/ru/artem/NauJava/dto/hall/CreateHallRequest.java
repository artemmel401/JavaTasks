package ru.artem.NauJava.dto.hall;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateHallRequest {
    @Schema(example = "name")
    private String name;

    @Schema(example = "50")
    private Integer seatsPerRow;

    @Schema(example = "148")
    private Integer totalRows;

    @Schema(example = "1")
    private Long cinema_id;
}
