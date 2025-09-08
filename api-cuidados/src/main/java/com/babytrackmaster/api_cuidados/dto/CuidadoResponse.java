package com.babytrackmaster.api_cuidados.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CuidadoResponse", description = "Representación de un cuidado")
public class CuidadoResponse {
        @Schema(example = "101")
    private Long id;
        @Schema(example = "1")
    private Long bebeId;
    private Long usuarioId;
    @Schema(example = "1")
    private Long tipoId;
    private String tipoNombre;
    private Date inicio;
    private Date fin;
    private String duracion;
    @Schema(example = "120")
    private Integer cantidadMl;
    private String tipoPanal;
    private String medicamento;
    private String dosis;
    private String observaciones;
    private Date createdAt;
    private Date updatedAt;
}
