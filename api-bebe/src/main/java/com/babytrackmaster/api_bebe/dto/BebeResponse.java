package com.babytrackmaster.api_bebe.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BebeResponse {
    private Long id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String sexo;
    private Double pesoNacer;
    private Double tallaNacer;
    private Integer semanasGestacion;
    private Double pesoActual;
    private Double tallaActual;
    private Boolean bebeActivo;
    private String numeroSs;
    private String grupoSanguineo;
    private String medicaciones;
    private String alergias;
    private String pediatra;
    private String centroMedico;
    private String telefonoCentroMedico;
    private String observaciones;
    private byte[] imagenBebe;
}
