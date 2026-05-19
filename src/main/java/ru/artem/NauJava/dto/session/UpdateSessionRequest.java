package ru.artem.NauJava.dto.session;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "UpdateSessionRequest")
public class UpdateSessionRequest {

    @Schema(example = "2024-01-15T14:30:00")
    @JsonProperty("start_time")
    @JsonAlias("startTime")
    private LocalDateTime startTime;

    @Schema(example = "2024-01-15T17:00:00")
    @JsonProperty("end_time")
    @JsonAlias("endTime")
    private LocalDateTime endTime;

    @Schema(example = "500")
    private Integer price;

    @Schema(example = "1")
    @JsonProperty("hall_id")
    @JsonAlias("hallId")
    private Long hallId;

    @Schema(example = "1")
    @JsonProperty("movie_id")
    @JsonAlias("movieId")
    private Long movieId;
}
