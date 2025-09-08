package com.babytrackmaster.api_crecimiento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "TipoCrecimientoResponse", description = "Representación de un tipo de crecimiento")
public class TipoCrecimientoResponse {
    @Schema(example = "1")
    private Long id;
    private String nombre;
}
