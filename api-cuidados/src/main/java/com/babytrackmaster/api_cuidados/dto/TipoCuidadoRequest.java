package com.babytrackmaster.api_cuidados.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "TipoCuidadoRequest", description = "Datos para crear/actualizar un tipo de cuidado")
public class TipoCuidadoRequest {
    @NotBlank
    @Size(min = 1, max = 60)
    @Schema(example = "Alimentación", description = "Nombre del tipo de cuidado")
    private String nombre;
}
