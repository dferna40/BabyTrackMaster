package com.babytrackmaster.api_cuidados.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CuidadoResponse {
    private Long id;
    private Long bebeId;
    private Long usuarioId;
    private String tipo;
    private Date inicio;
    private Date fin;
    private Integer cantidadMl;
    private String pecho;
    private String tipoPanal;
    private String medicamento;
    private String dosis;
    private String observaciones;
    private Date createdAt;
    private Date updatedAt;
}
