package com.babytrackmaster.api_hitos.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HitoResponse {
    private Long id;
    private Long bebeId;
    private String titulo;
    private LocalDate fecha;
    private String descripcion;
    private String imagenUrl;
}