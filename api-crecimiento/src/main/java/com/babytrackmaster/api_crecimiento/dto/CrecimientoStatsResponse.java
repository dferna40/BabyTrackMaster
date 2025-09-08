package com.babytrackmaster.api_crecimiento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CrecimientoStatsResponse", description = "Estadísticas básicas de crecimiento")
public class CrecimientoStatsResponse {
    @Schema(example = "3.2", description = "Valor mínimo registrado")
    private Double min;
    @Schema(example = "4.5", description = "Valor máximo registrado")
    private Double max;
    @Schema(example = "3.8", description = "Valor promedio")
    private Double avg;
}
