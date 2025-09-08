package com.babytrackmaster.api_crecimiento.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CrecimientoResponse", description = "Representación de un registro de crecimiento")
public class CrecimientoResponse {
    @Schema(example = "101")
    private Long id;
    @Schema(example = "1")
    private Long bebeId;
    private Long usuarioId;
    @Schema(example = "1")
    private Long tipoId;
    private String tipoNombre;
    private Date fecha;
    private Double valor;
    private String unidad;
    private String observaciones;
    private Date createdAt;
    private Date updatedAt;
}
