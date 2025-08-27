package com.babytrackmaster.api_gastos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "GastoUpdateRequest", description = "Datos para actualizar un gasto")
public class GastoUpdateRequest {
    @NotNull
    @Schema(example = "2", description = "ID de la categoría del gasto")
    private Long categoriaId;

    @NotNull
    @DecimalMin("0.00")
    @Schema(example = "19.90", description = "Importe del gasto")
    private BigDecimal cantidad;

    @NotNull
    @Schema(type = "string", format = "date", example = "2025-08-28", description = "Fecha del gasto (YYYY-MM-DD)")
    private LocalDate fecha;

    @Size(max = 255)
    @Schema(example = "Compra semanal de pañales", description = "Descripción opcional")
    private String descripcion;

    @Schema(example = "1", description = "ID del bebé (opcional)")
    private Long bebeId;
}