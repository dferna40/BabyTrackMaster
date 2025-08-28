package com.babytrackmaster.api_citas.mapper;

import com.babytrackmaster.api_citas.dto.*;
import com.babytrackmaster.api_citas.entity.Cita;
import com.babytrackmaster.api_citas.entity.TipoCita;
import com.babytrackmaster.api_citas.entity.EstadoCita;
import com.babytrackmaster.api_citas.mapper.EstadoCitaMapper;
import java.time.*;

public class CitaMapper {

    private CitaMapper() {}

    public static Cita toEntity(CitaCreateDTO dto, Long usuarioId, TipoCita tipo, EstadoCita estado) {
        Cita c = new Cita();
        c.setUsuarioId(usuarioId);
        c.setTitulo(dto.getTitulo());
        c.setDescripcion(dto.getDescripcion());
        c.setFecha(LocalDate.parse(dto.getFecha()));
        c.setHora(LocalTime.parse(dto.getHora()));
        c.setUbicacion(dto.getUbicacion());
        c.setMedico(dto.getMedico());
        c.setTipo(tipo);
        c.setRecordatorioMinutos(dto.getRecordatorioMinutos());
        c.setEstado(estado);
        c.setEliminado(Boolean.FALSE);
        return c;
    }

    public static void applyUpdate(Cita c, CitaUpdateDTO dto, TipoCita tipo, EstadoCita estado) {
        if (dto.getTitulo() != null) c.setTitulo(dto.getTitulo());
        if (dto.getDescripcion() != null) c.setDescripcion(dto.getDescripcion());
        if (dto.getFecha() != null) c.setFecha(LocalDate.parse(dto.getFecha()));
        if (dto.getHora() != null) c.setHora(LocalTime.parse(dto.getHora()));
        if (dto.getUbicacion() != null) c.setUbicacion(dto.getUbicacion());
        if (dto.getMedico() != null) c.setMedico(dto.getMedico());
        if (tipo != null) c.setTipo(tipo);
        if (dto.getRecordatorioMinutos() != null) c.setRecordatorioMinutos(dto.getRecordatorioMinutos());
        if (estado != null) c.setEstado(estado);
    }

    public static CitaResponseDTO toDTO(Cita c) {
        CitaResponseDTO.CitaResponseDTOBuilder b = CitaResponseDTO.builder();
        b.id(c.getId());
        b.usuarioId(c.getUsuarioId());
        b.titulo(c.getTitulo());
        b.descripcion(c.getDescripcion());
        b.fecha(c.getFecha() != null ? c.getFecha().toString() : null);
        b.hora(c.getHora() != null ? c.getHora().toString() : null);
        b.ubicacion(c.getUbicacion());
        b.medico(c.getMedico());
        if (c.getTipo() != null) {
            b.tipo(TipoCitaMapper.toDTO(c.getTipo()));
        }
        if (c.getEstado() != null) {
            b.estado(EstadoCitaMapper.toDTO(c.getEstado()));
        }
        b.recordatorioMinutos(c.getRecordatorioMinutos());
        return b.build();
    }
}
