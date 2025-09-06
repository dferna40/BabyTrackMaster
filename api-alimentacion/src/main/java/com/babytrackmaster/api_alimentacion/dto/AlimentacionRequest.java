package com.babytrackmaster.api_alimentacion.dto;

import java.util.Date;

import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "AlimentacionRequest", description = "Datos para crear/actualizar un registro de alimentacion")
public class AlimentacionRequest {
    @NotNull
    private TipoAlimentacion tipo;
    @NotNull
    private Date fechaHora;

    // Lactancia
    private String lado;
    private Integer duracionMin;

    // Biberon
    private String tipoLeche;
    private Integer cantidadMl;

    // Solidos
    private String alimento;
    private String cantidad;
    private String observaciones;
}
