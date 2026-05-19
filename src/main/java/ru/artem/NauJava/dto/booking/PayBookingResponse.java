package ru.artem.NauJava.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "PayBookingResponse")
public class PayBookingResponse {

    @Schema(example = "true")
    private boolean success;

    @Schema(example = "Payment processed successfully")
    private String message;
}
