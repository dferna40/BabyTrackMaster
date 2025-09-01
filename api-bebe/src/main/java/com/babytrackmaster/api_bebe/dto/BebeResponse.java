package com.babytrackmaster.api_bebe.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BebeResponse {
    private Long id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String sexo;
}
