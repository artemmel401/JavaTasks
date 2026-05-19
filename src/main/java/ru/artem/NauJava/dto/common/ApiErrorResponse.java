package ru.artem.NauJava.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiErrorResponse")
public class ApiErrorResponse {

    @Schema(example = "Кинотеатр с ID 1 не найден")
    private String error;
}
