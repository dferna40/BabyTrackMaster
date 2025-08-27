package com.babytrackmaster.api_citas.dto;

import com.babytrackmaster.api_citas.enums.EstadoCita;
import com.babytrackmaster.api_citas.enums.TipoCita;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class CitaUpdateDTO {

    @Size(max = 150)
    private String titulo;

    @Size(max = 500)
    private String descripcion;

    // opcionales, si vienen se actualizan
    @Schema(example = "2025-09-16")
    private String fecha;

    @Schema(example = "11:00")
    private String hora;

    @Size(max = 150)
    private String ubicacion;

    @Size(max = 120)
    private String medico;

    private TipoCita tipo;

    private Integer recordatorioMinutos;

    private EstadoCita estado;
}