package ru.artem.NauJava.dto.movie;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateMovieRequest {

    @Schema(example = "Inception")
    private String title;

    @Schema(example = "A thief who steals corporate secrets")
    private String description;

    @Schema(example = "148")
    private Integer durationMinutes;

    @Schema(example = "http://example.com/poster.jpg")
    private String posterUrl;

    @Schema(example = "1")
    private Long cinema_id;

}
