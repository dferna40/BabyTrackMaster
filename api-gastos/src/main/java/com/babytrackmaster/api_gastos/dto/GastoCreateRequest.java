package com.babytrackmaster.api_gastos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "GastoCreateRequest", description = "Datos para crear un gasto")
public class GastoCreateRequest {
    @NotNull
    @Schema(example = "2", description = "ID de la categoría del gasto")
    private Long categoriaId;

    @NotNull
    @DecimalMin("0.00")
    @Schema(example = "23.50", description = "Importe del gasto")
    private BigDecimal cantidad;

    @NotNull
    @Schema(type = "string", format = "date", example = "2025-08-27", description = "Fecha del gasto (YYYY-MM-DD)")
    private LocalDate fecha;

    @Size(max = 255)
    @Schema(example = "Pañales y toallitas", description = "Descripción opcional")
    private String descripcion;

    // Opcional si manejas multi-bebé
    @Schema(example = "1", description = "ID del bebé (opcional)")
    private Long bebeId;
}
