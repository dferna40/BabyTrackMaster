package com.babytrackmaster.api_rutinas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "RutinaEjecucionCreateDTO", description = "Datos para registrar una ejecución de rutina")
public class RutinaEjecucionCreateDTO {

	@NotBlank
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2})?$", message = "fechaHora debe tener formato ISO-8601: YYYY-MM-DDTHH:mm o YYYY-MM-DDTHH:mm:ss")
	@Schema(example = "2025-08-27T08:30:00", description = "Fecha/hora de ejecución (ISO-8601)")
	private String fechaHora; // ISO-8601: "2025-08-27T08:30:00"

	@Size(max = 20)
	@Schema(example = "OK", description = "Estado de la ejecución (OK, FALLIDA, OMITIDA)")
	private String estado;

	@Size(max = 300)
	@Schema(example = "Se completó sin incidencias", description = "Nota opcional")
	private String nota;
}