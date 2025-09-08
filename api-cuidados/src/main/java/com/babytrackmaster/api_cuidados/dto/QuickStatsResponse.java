package com.babytrackmaster.api_cuidados.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "QuickStatsResponse", description = "Estadísticas rápidas de cuidados para un día")
public class QuickStatsResponse {
    @Schema(example = "8.0", description = "Horas totales de sueño")
    private double horasSueno;
    @Schema(example = "5", description = "Cantidad de pañales")
    private long panales;
    @Schema(example = "1", description = "Cantidad de baños")
    private long banos;
}
