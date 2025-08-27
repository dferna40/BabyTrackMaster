package com.babytrackmaster.api_rutinas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RutinaDTO {
    private Long id;
    @Schema(example = "Paseo de la mañana")
    private String nombre;
    @Schema(example = "15 min por el parque")
    private String descripcion;
    @Schema(example = "09:30")
    private String horaProgramada; // "HH:mm"
    @Schema(example = "L,M,X,J,V")
    private String diasSemana;
    private Boolean activa;
}
