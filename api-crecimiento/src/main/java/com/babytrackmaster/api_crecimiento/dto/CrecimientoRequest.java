package com.babytrackmaster.api_crecimiento.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "CrecimientoRequest", description = "Datos para crear/actualizar un registro de crecimiento")
public class CrecimientoRequest {
    @Schema(example = "1", description = "ID del bebé")
    @NotNull private Long bebeId;

    @Schema(example = "1", description = "ID del tipo de crecimiento")
    @NotNull private Long tipoId;

    @NotNull private Date fecha;

    @NotNull private Double valor;

    private String unidad;

    private String observaciones;
}
