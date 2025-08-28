package com.babytrackmaster.api_citas.mapper;

import com.babytrackmaster.api_citas.dto.EstadoCitaCreateDTO;
import com.babytrackmaster.api_citas.dto.EstadoCitaResponseDTO;
import com.babytrackmaster.api_citas.dto.EstadoCitaUpdateDTO;
import com.babytrackmaster.api_citas.entity.EstadoCita;

public class EstadoCitaMapper {

    private EstadoCitaMapper() {}

    public static EstadoCita toEntity(EstadoCitaCreateDTO dto) {
        return EstadoCita.builder()
                .nombre(dto.getNombre())
                .build();
    }

    public static void applyUpdate(EstadoCita entity, EstadoCitaUpdateDTO dto) {
        if (dto.getNombre() != null) {
            entity.setNombre(dto.getNombre());
        }
    }

    public static EstadoCitaResponseDTO toDTO(EstadoCita entity) {
        if (entity == null) return null;
        return EstadoCitaResponseDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }
}
