package com.babytrackmaster.api_gastos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "GastoResponse", description = "Representación de un gasto")
public class GastoResponse {
	@Schema(example = "101")
    private Long id;
	@Schema(example = "2")
    private Long categoriaId;
	@Schema(example = "Pañales", description = "Nombre de la categoría")
    private String categoriaNombre;
	@Schema(example = "23.50")
    private BigDecimal cantidad;
	@Schema(type = "string", format = "date", example = "2025-08-27")
    private LocalDate fecha;
	@Schema(example = "Pañales y toallitas")
    private String descripcion;
	@Schema(example = "1")
    private Long bebeId;
}
