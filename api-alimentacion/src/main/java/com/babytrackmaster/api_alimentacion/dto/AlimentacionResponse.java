package com.babytrackmaster.api_alimentacion.dto;

import java.time.LocalDateTime;

import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoLactancia;
import com.babytrackmaster.api_alimentacion.entity.TipoLecheBiberon;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacionSolido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AlimentacionResponse", description = "Representación de un registro de alimentacion")
public class AlimentacionResponse {
    private Long id;
    private Long usuarioId;
    private Long bebeId;
    private TipoAlimentacion tipoAlimentacion;
    private LocalDateTime fechaHora;
    private String lado;
    private Integer duracionMin;
    private TipoLactancia tipoLactancia;
    private TipoLecheBiberon tipoBiberon;
    private Integer cantidadMl;
    private Integer cantidadLecheFormula;
    private TipoAlimentacionSolido tipoAlimentacionSolido;
    private String cantidad;
    private Integer cantidadOtrosAlimentos;
    private String alimentacionOtros;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
