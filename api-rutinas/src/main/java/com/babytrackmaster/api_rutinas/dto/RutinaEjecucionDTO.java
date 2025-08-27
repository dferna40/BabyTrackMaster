package com.babytrackmaster.api_rutinas.dto;

import lombok.Data;

@Data
public class RutinaEjecucionDTO {
    private Long id;
    private Long rutinaId;
    private String fechaHora;
    private String estado;
    private String nota;
}