package ru.artem.NauJava.dto.hall;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "UpdateHallRequest")
public class UpdateHallRequest {

    @Schema(example = "Зал 1")
    private String name;

    @Schema(example = "20")
    @JsonAlias("seats_per_row")
    private Integer seatsPerRow;

    @Schema(example = "15")
    @JsonAlias("total_rows")
    private Integer totalRows;

    @Schema(example = "1")
    @JsonProperty("cinema_id")
    @JsonAlias("cinemaId")
    private Long cinemaId;
}
