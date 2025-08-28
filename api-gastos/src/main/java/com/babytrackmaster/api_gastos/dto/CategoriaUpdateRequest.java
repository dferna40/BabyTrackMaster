package com.babytrackmaster.api_gastos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "CategoriaUpdateRequest", description = "Datos para actualizar una categoría de gasto")
public class CategoriaUpdateRequest {
    @NotBlank
    @Schema(example = "Pañales", description = "Nombre de la categoría")
    private String nombre;
}
