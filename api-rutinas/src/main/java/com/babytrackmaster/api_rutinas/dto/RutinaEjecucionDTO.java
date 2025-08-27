package com.babytrackmaster.api_rutinas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "RutinaEjecucionDTO", description = "Datos de una rutina en ejecución")
public class RutinaEjecucionDTO {
    private Long id;
    private Long rutinaId;
    private String fechaHora;
    private String estado;
    private String nota;
}