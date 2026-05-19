package ru.artem.NauJava.dto.movie;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "UpdateMovieRequest")
public class UpdateMovieRequest {

    @Schema(example = "Inception")
    private String title;

    @Schema(example = "Описание фильма")
    private String description;

    @Schema(example = "148")
    @JsonAlias("duration_minutes")
    private Integer durationMinutes;

    @Schema(example = "https://example.com/poster.jpg")
    @JsonAlias("poster_url")
    private String posterUrl;

    @Schema(example = "1")
    @JsonProperty("cinema_id")
    @JsonAlias("cinemaId")
    private Long cinemaId;
}
