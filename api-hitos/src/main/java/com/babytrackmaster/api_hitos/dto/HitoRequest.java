package com.babytrackmaster.api_hitos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "HitoRequest", description = "Datos para crear/actualizar un hito del bebé")
public class HitoRequest {

    @NotNull
    @Schema(example = "1", description = "ID del bebé al que pertenece el hito")
    private Long bebeId;

    @NotNull
    @Size(min = 1, max = 150)
    @Schema(example = "Primera sonrisa", description = "Título corto del hito")
    private String titulo;

    @NotNull
    @Schema(type = "string", format = "date", example = "2025-08-25", description = "Fecha del hito (YYYY-MM-DD)")
    private LocalDate fecha;

    @Size(max = 1000)
    @Schema(example = "El bebé sonrió cuando jugábamos por la mañana.", description = "Descripción opcional")
    private String descripcion;

    @Size(max = 500)
    @Schema(example = "https://mi-cdn/imagenes/sonrisa.jpg", description = "URL opcional de una imagen asociada")
    private String imagenUrl;
}
