package com.babytrackmaster.api_cuidados.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TipoCuidado", description = "Tipo de cuidado registrado")
public enum TipoCuidado {
	@Schema(description = "Toma de leche materna o biberón") ALIMENTACION,
    @Schema(description = "Cambio de pañal") HIGIENE,
    @Schema(description = "Baño") BANO,
    @Schema(description = "Siesta o sueño nocturno") SUENO,
    @Schema(description = "Administración de medicación") MEDICACION,
    @Schema(description = "Paseo o actividad física") PASEO
}