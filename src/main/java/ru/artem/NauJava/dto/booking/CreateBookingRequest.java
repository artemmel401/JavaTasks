package ru.artem.NauJava.dto.booking;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBookingRequest {
    @Schema(example = "1000")
    @JsonProperty("total_amount")
    @JsonAlias("totalAmount")
    private Integer total_amount;

    @Schema(example = "1")
    @JsonProperty("session_id")
    @JsonAlias("sessionId")
    private Long session_id;
}
