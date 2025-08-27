package com.babytrackmaster.api_citas.dto;

import com.babytrackmaster.api_citas.enums.TipoCita;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CitaCreateDTO {

    @Schema(example = "Revisión 1 mes")
    @NotBlank
    @Size(max = 150)
    private String titulo;

    @Schema(example = "Control de peso y lactancia")
    @Size(max = 500)
    private String descripcion;

    @Schema(example = "2025-09-15")
    @NotNull
    private String fecha; // ISO yyyy-MM-dd

    @Schema(example = "10:30")
    @NotNull
    private String hora; // HH:mm

    @Schema(example = "Centro de Salud Los Álamos")
    @Size(max = 150)
    private String ubicacion;

    @Schema(example = "Dra. García")
    @Size(max = 120)
    private String medico;

    @Schema(example = "PEDIATRIA")
    @NotNull
    private TipoCita tipo;

    @Schema(example = "30")
    private Integer recordatorioMinutos;
}