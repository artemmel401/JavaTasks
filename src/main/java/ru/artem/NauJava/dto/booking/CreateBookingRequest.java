package ru.artem.NauJava.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBookingRequest {
    @Schema(example = "1000")
    private Integer total_amount;

    @Schema(example = "1")
    private Long session_id;
}
