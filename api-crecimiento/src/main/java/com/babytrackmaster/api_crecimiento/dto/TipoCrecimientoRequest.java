package com.babytrackmaster.api_crecimiento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "TipoCrecimientoRequest", description = "Datos para crear/actualizar un tipo de crecimiento")
public class TipoCrecimientoRequest {
    @Schema(example = "Peso")
    @NotBlank
    private String nombre;
}
