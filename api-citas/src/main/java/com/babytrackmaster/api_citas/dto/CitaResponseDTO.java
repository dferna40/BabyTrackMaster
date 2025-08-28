package com.babytrackmaster.api_citas.dto;

import com.babytrackmaster.api_citas.enums.EstadoCita;
import lombok.*;

@Getter @Setter @Builder
public class CitaResponseDTO {
    private Long id;
    private Long usuarioId;
    private String titulo;
    private String descripcion;
    private String fecha; // ISO
    private String hora;  // HH:mm
    private String ubicacion;
    private String medico;
    private TipoCitaResponseDTO tipo;
    private EstadoCita estado;
    private Integer recordatorioMinutos;
    private String creadoEn;
    private String actualizadoEn;
}
