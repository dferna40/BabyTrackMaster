package com.babytrackmaster.api_rutinas.mapper;

import java.time.LocalDateTime;

import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionDTO;
import com.babytrackmaster.api_rutinas.entity.Rutina;
import com.babytrackmaster.api_rutinas.entity.RutinaEjecucion;

public class RutinaEjecucionMapper {

    public static RutinaEjecucion toEntity(RutinaEjecucionCreateDTO dto, Rutina rutina) {
        RutinaEjecucion e = new RutinaEjecucion();
        e.setRutina(rutina);
        e.setFechaHora(LocalDateTime.parse(dto.getFechaHora()));
        e.setEstado(dto.getEstado());
        e.setNota(dto.getNota());
        return e;
    }

    public static RutinaEjecucionDTO toDTO(RutinaEjecucion e) {
        RutinaEjecucionDTO dto = new RutinaEjecucionDTO();
        dto.setId(e.getId());
        dto.setRutinaId(e.getRutina() != null ? e.getRutina().getId() : null);
        dto.setFechaHora(e.getFechaHora() != null ? e.getFechaHora().toString() : null);
        dto.setEstado(e.getEstado());
        dto.setNota(e.getNota());
        return dto;
    }
}