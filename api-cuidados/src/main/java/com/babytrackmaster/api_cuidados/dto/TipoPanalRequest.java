package com.babytrackmaster.api_cuidados.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "TipoPanalRequest", description = "Datos para crear/actualizar un tipo de pañal")
public class TipoPanalRequest {
    @NotBlank
    @Size(min = 1, max = 60)
    @Schema(example = "PIPI", description = "Nombre del tipo de pañal")
    private String nombre;
}

