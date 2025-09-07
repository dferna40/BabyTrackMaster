package com.babytrackmaster.api_citas.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class CitaUpdateDTO {

    @Size(max = 150)
    private String motivo;

    @Size(max = 500)
    private String descripcion;

    // opcionales, si vienen se actualizan
    @Schema(example = "2025-09-16")
    private String fecha;

    @Schema(example = "11:00")
    private String hora;

    @Size(max = 150)
    private String centroMedico;

    @Size(max = 120)
    private String medico;

    private Long bebeId;

    private Long tipoId;

    private Integer recordatorioMinutos;

    private Long estadoId;
}