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
        c.setBebeId(dto.getBebeId());
        c.setMotivo(dto.getMotivo());
        c.setDescripcion(dto.getDescripcion());
        c.setFecha(LocalDate.parse(dto.getFecha()));
        c.setHora(LocalTime.parse(dto.getHora()));
        c.setCentroMedico(dto.getCentroMedico());
        c.setMedico(dto.getMedico());
        c.setTipo(tipo);
        c.setRecordatorioMinutos(dto.getRecordatorioMinutos());
        c.setEstado(estado);
        c.setEliminado(Boolean.FALSE);
        return c;
    }

    public static void applyUpdate(Cita c, CitaUpdateDTO dto, TipoCita tipo, EstadoCita estado) {
        if (dto.getMotivo() != null) c.setMotivo(dto.getMotivo());
        if (dto.getDescripcion() != null) c.setDescripcion(dto.getDescripcion());
        if (dto.getFecha() != null) c.setFecha(LocalDate.parse(dto.getFecha()));
        if (dto.getHora() != null) c.setHora(LocalTime.parse(dto.getHora()));
        if (dto.getCentroMedico() != null) c.setCentroMedico(dto.getCentroMedico());
        if (dto.getMedico() != null) c.setMedico(dto.getMedico());
        if (dto.getBebeId() != null) c.setBebeId(dto.getBebeId());
        if (tipo != null) c.setTipo(tipo);
        if (dto.getRecordatorioMinutos() != null) c.setRecordatorioMinutos(dto.getRecordatorioMinutos());
        if (estado != null) c.setEstado(estado);
    }

    public static CitaResponseDTO toDTO(Cita c) {
        CitaResponseDTO.CitaResponseDTOBuilder b = CitaResponseDTO.builder();
        b.id(c.getId());
        b.usuarioId(c.getUsuarioId());
        b.bebeId(c.getBebeId());
        b.motivo(c.getMotivo());
        b.descripcion(c.getDescripcion());
        b.fecha(c.getFecha() != null ? c.getFecha().toString() : null);
        b.hora(c.getHora() != null ? c.getHora().toString() : null);
        b.centroMedico(c.getCentroMedico());
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
