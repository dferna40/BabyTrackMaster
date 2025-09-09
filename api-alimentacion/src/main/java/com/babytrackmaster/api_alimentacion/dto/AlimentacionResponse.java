package com.babytrackmaster.api_alimentacion.dto;

import java.util.Date;

import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoLactancia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AlimentacionResponse", description = "Representación de un registro de alimentacion")
public class AlimentacionResponse {
    private Long id;
    private Long usuarioId;
    private Long bebeId;
    @Schema(allowableValues = {"lactancia", "biberon", "solidos"})
    private TipoAlimentacion tipo;
    private Date fechaHora;
    private String lado;
    private Integer duracionMin;
    private TipoLactancia tipoLactancia;
    private String tipoLeche;
    private Integer cantidadMl;
    private Integer cantidadLecheFormula;
    private String alimento;
    private String cantidad;
    private Integer cantidadOtrosAlimentos;
    private String observaciones;
    private Date createdAt;
    private Date updatedAt;
}
