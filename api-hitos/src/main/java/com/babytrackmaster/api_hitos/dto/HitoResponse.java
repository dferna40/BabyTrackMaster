package com.babytrackmaster.api_hitos.dto;

import lombok.Data;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "HitoResponse", description = "Representación de un hito")
public class HitoResponse {
	@Schema(example = "12")
    private Long id;
	@Schema(example = "1", description = "ID del bebé")
    private Long bebeId;
	@Schema(example = "Primera sonrisa")
    private String titulo;
	@Schema(type = "string", format = "date", example = "2025-08-25")
    private LocalDate fecha;
	@Schema(example = "El bebé sonrió cuando jugábamos por la mañana.")
    private String descripcion;
	@Schema(example = "https://mi-cdn/imagenes/sonrisa.jpg")
    private String imagenUrl;
}