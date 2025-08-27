package com.babytrackmaster.api_rutinas.dto;

import lombok.Data;

@Data
public class RutinaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String horaProgramada; // "HH:mm"
    private String diasSemana;
    private Boolean activa;
}
