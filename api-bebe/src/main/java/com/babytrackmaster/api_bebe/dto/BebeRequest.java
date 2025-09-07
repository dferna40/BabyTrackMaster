package com.babytrackmaster.api_bebe.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BebeRequest {
    @NotBlank
    private String nombre;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotBlank
    private String sexo;

    @PositiveOrZero
    private Double pesoNacer;

    @PositiveOrZero
    private Double tallaNacer;

    @PositiveOrZero
    private Integer semanasGestacion;

    @PositiveOrZero
    private Double perimetroCranealNacer;

    @PositiveOrZero
    private Double pesoActual;

    @PositiveOrZero
    private Double tallaActual;

    private Boolean bebeActivo;

    @Size(max = 50)
    private String numeroSs;

    @Size(max = 500)
    private String medicaciones;

    private Long tipoLactanciaId;

    private Long tipoAlergiaId;

    private Long tipoGrupoSanguineoId;

    @Size(max = 100)
    private String pediatra;

    @Size(max = 255)
    private String centroMedico;

    @Size(max = 20)
    private String telefonoCentroMedico;

    @Size(max = 1000)
    private String observaciones;

    private byte[] imagenBebe;
}
