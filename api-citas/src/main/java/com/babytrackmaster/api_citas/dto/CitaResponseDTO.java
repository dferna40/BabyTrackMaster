package com.babytrackmaster.api_citas.dto;

import com.babytrackmaster.api_citas.dto.EstadoCitaResponseDTO;
import lombok.*;

@Getter @Setter @Builder
public class CitaResponseDTO {
    private Long id;
    private Long usuarioId;
    private Long bebeId;
    private String motivo;
    private String descripcion;
    private String fecha; // ISO
    private String hora;  // HH:mm
    private String centroMedico;
    private String medico;
    private TipoCitaResponseDTO tipo;
    private EstadoCitaResponseDTO estado;
    private Integer recordatorioMinutos;
    private String creadoEn;
    private String actualizadoEn;
}
