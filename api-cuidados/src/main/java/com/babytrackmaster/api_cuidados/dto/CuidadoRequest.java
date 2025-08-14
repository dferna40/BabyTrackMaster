package com.babytrackmaster.api_cuidados.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CuidadoRequest {
    @NotNull private Long bebeId;
    @NotNull private Long usuarioId;
    @NotNull private TipoCuidado tipo;
    @NotNull private Date inicio;

    private Date fin;
    private Integer cantidadMl;
    private String pecho;
    private String tipoPanal;
    private String medicamento;
    private String dosis;
    private String observaciones;
}
