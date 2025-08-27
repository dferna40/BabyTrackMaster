package com.babytrackmaster.api_citas.mapper;

import com.babytrackmaster.api_citas.dto.*;
import com.babytrackmaster.api_citas.entity.Cita;
import com.babytrackmaster.api_citas.enums.*;
import java.time.*;

public class CitaMapper {

    private CitaMapper() {}

    public static Cita toEntity(CitaCreateDTO dto, Long usuarioId) {
        Cita c = new Cita();
        c.setUsuarioId(usuarioId);
        c.setTitulo(dto.getTitulo());
        c.setDescripcion(dto.getDescripcion());
        c.setFecha(LocalDate.parse(dto.getFecha()));
        c.setHora(LocalTime.parse(dto.getHora()));
        c.setUbicacion(dto.getUbicacion());
        c.setMedico(dto.getMedico());
        c.setTipo(dto.getTipo());
        c.setRecordatorioMinutos(dto.getRecordatorioMinutos());
        c.setEstado(EstadoCita.PENDIENTE);
        c.setEliminado(Boolean.FALSE);
        return c;
    }

    public static void applyUpdate(Cita c, CitaUpdateDTO dto) {
        if (dto.getTitulo() != null) c.setTitulo(dto.getTitulo());
        if (dto.getDescripcion() != null) c.setDescripcion(dto.getDescripcion());
        if (dto.getFecha() != null) c.setFecha(LocalDate.parse(dto.getFecha()));
        if (dto.getHora() != null) c.setHora(LocalTime.parse(dto.getHora()));
        if (dto.getUbicacion() != null) c.setUbicacion(dto.getUbicacion());
        if (dto.getMedico() != null) c.setMedico(dto.getMedico());
        if (dto.getTipo() != null) c.setTipo(dto.getTipo());
        if (dto.getRecordatorioMinutos() != null) c.setRecordatorioMinutos(dto.getRecordatorioMinutos());
        if (dto.getEstado() != null) c.setEstado(dto.getEstado());
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
        b.tipo(c.getTipo());
        b.estado(c.getEstado());
        b.recordatorioMinutos(c.getRecordatorioMinutos());
        return b.build();
    }
}
