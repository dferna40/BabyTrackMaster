package com.babytrackmaster.api_citas.mapper;

import com.babytrackmaster.api_citas.dto.TipoCitaCreateDTO;
import com.babytrackmaster.api_citas.dto.TipoCitaResponseDTO;
import com.babytrackmaster.api_citas.dto.TipoCitaUpdateDTO;
import com.babytrackmaster.api_citas.entity.TipoCita;

public class TipoCitaMapper {

    private TipoCitaMapper() {}

    public static TipoCita toEntity(TipoCitaCreateDTO dto) {
        return TipoCita.builder()
                .nombre(dto.getNombre())
                .build();
    }

    public static void applyUpdate(TipoCita entity, TipoCitaUpdateDTO dto) {
        if (dto.getNombre() != null) {
            entity.setNombre(dto.getNombre());
        }
    }

    public static TipoCitaResponseDTO toDTO(TipoCita entity) {
        if (entity == null) return null;
        return TipoCitaResponseDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }
}
