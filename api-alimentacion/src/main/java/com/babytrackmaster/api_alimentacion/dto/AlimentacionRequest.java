package com.babytrackmaster.api_alimentacion.dto;

import java.time.LocalDateTime;

import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoLactancia;
import com.babytrackmaster.api_alimentacion.entity.TipoLecheBiberon;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacionSolido;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "AlimentacionRequest", description = "Datos para crear/actualizar un registro de alimentacion")
public class AlimentacionRequest {
    @NotNull
    private TipoAlimentacion tipoAlimentacion;
    private LocalDateTime fechaHora;

    // Lactancia
    private String lado;
    private Integer duracionMin;
    private TipoLactancia tipoLactancia;

    // Biberon
    private TipoLecheBiberon tipoBiberon;
    private Integer cantidadMl;
    private Integer cantidadLecheFormula;

    // Solidos
    private TipoAlimentacionSolido tipoAlimentacionSolido;
    private String cantidad;
    private Integer cantidadOtrosAlimentos;
    private String alimentacionOtros;
    private String observaciones;
}
