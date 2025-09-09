package com.babytrackmaster.api_citas.dto;


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
    private String motivo;

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
    private String centroMedico;

    @Schema(example = "Dra. García")
    @Size(max = 120)
    private String medico;

    @Schema(example = "1")
    @NotNull
    private Long bebeId;

    @Schema(example = "1")
    @NotNull
    private Long tipoId;

    @Schema(example = "1", description = "Identificador del tipo de especialidad (opcional)")
    private Long tipoEspecialidadId;

    @Schema(example = "30")
    private Integer recordatorioMinutos;
}