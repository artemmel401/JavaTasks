package ru.artem.NauJava.dto.session;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreateSessionRequest {

    @Schema(example = "2024-01-15T14:30:45.000")
    private LocalDateTime start_time;

    @Schema(example = "2024-01-15T14:30:45.000")
    private LocalDateTime end_time;

    @Schema(example = "148")
    private Integer price;

    @Schema(example = "1")
    private Long hall_id;

    @Schema(example = "1")
    private Long movie_id;

}
