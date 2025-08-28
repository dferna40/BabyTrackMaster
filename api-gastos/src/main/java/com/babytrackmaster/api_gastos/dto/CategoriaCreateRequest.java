package com.babytrackmaster.api_gastos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "CategoriaCreateRequest", description = "Datos para crear una categoría de gasto")
public class CategoriaCreateRequest {
    @NotBlank
    @Schema(example = "Pañales", description = "Nombre de la categoría")
    private String nombre;
}
