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
    private Double perimetroCranealNacer;
    private Double pesoActual;
    private Double tallaActual;
    private Boolean bebeActivo;
    private String numeroSs;
    private String medicaciones;
    private Long tipoLactanciaId;
    private String tipoLactanciaNombre;
    private Long tipoAlergiaId;
    private String tipoAlergiaNombre;
    private Long tipoGrupoSanguineoId;
    private String tipoGrupoSanguineoNombre;
    private String pediatra;
    private String centroMedico;
    private String telefonoCentroMedico;
    private String observaciones;
    private byte[] imagenBebe;
}
