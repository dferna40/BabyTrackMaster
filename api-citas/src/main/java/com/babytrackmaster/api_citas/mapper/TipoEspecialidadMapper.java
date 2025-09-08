package com.babytrackmaster.api_citas.mapper;

import com.babytrackmaster.api_citas.dto.TipoEspecialidadResponseDTO;
import com.babytrackmaster.api_citas.entity.TipoEspecialidad;

public class TipoEspecialidadMapper {

    private TipoEspecialidadMapper() {}

    public static TipoEspecialidadResponseDTO toDTO(TipoEspecialidad entity) {
        if (entity == null) return null;
        return TipoEspecialidadResponseDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }
}
