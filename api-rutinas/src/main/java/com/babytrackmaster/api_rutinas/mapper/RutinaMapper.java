package com.babytrackmaster.api_rutinas.mapper;

import java.time.LocalTime;

import com.babytrackmaster.api_rutinas.dto.RutinaCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaDTO;
import com.babytrackmaster.api_rutinas.entity.Rutina;

public class RutinaMapper {

    public static Rutina toEntity(RutinaCreateDTO dto, Long usuarioId) {
        Rutina r = new Rutina();
        r.setUsuarioId(usuarioId);
        r.setNombre(dto.getNombre());
        r.setDescripcion(dto.getDescripcion());
        r.setHoraProgramada(LocalTime.parse(dto.getHoraProgramada()));
        r.setDiasSemana(dto.getDiasSemana());
        if (dto.getActiva() == null) {
            r.setActiva(Boolean.TRUE);
        } else {
            r.setActiva(dto.getActiva());
        }
        return r;
    }

    public static void updateEntity(Rutina r, RutinaCreateDTO dto) {
        if (dto.getNombre() != null) r.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) r.setDescripcion(dto.getDescripcion());
        if (dto.getHoraProgramada() != null) r.setHoraProgramada(LocalTime.parse(dto.getHoraProgramada()));
        if (dto.getDiasSemana() != null) r.setDiasSemana(dto.getDiasSemana());
        if (dto.getActiva() != null) r.setActiva(dto.getActiva());
    }

    public static RutinaDTO toDTO(Rutina r) {
        RutinaDTO dto = new RutinaDTO();
        dto.setId(r.getId());
        dto.setNombre(r.getNombre());
        dto.setDescripcion(r.getDescripcion());
        dto.setHoraProgramada(r.getHoraProgramada() != null ? r.getHoraProgramada().toString() : null);
        dto.setDiasSemana(r.getDiasSemana());
        dto.setActiva(r.getActiva());
        return dto;
    }
}