package com.babytrackmaster.api_alimentacion.dto;

import java.util.Date;

import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoLactancia;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "AlimentacionRequest", description = "Datos para crear/actualizar un registro de alimentacion")
public class AlimentacionRequest {
    @NotNull
    @Schema(allowableValues = {"lactancia", "biberon", "solidos"})
    private TipoAlimentacion tipo;
    private Date fechaHora;

    // Lactancia
    private String lado;
    private Integer duracionMin;
    private TipoLactancia tipoLactancia;

    // Biberon
    private String tipoLeche;
    private Integer cantidadMl;
    private Integer cantidadLecheFormula;

    // Solidos
    private String alimento;
    private String cantidad;
    private Integer cantidadOtrosAlimentos;
    private String observaciones;
}
