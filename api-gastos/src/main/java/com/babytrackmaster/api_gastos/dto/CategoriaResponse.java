package com.babytrackmaster.api_gastos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CategoriaResponse", description = "Representación de una categoría de gasto")
public class CategoriaResponse {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "Pañales")
    private String nombre;
}
