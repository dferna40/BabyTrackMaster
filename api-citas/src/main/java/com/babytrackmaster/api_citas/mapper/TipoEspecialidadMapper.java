package com.babytrackmaster.api_citas.mapper;

import com.babytrackmaster.api_citas.dto.TipoEspecialidadCreateDTO;
import com.babytrackmaster.api_citas.dto.TipoEspecialidadResponseDTO;
import com.babytrackmaster.api_citas.dto.TipoEspecialidadUpdateDTO;
import com.babytrackmaster.api_citas.entity.TipoEspecialidad;

public class TipoEspecialidadMapper {

    private TipoEspecialidadMapper() {}

    public static TipoEspecialidad toEntity(TipoEspecialidadCreateDTO dto) {
        return TipoEspecialidad.builder()
                .nombre(dto.getNombre())
                .build();
    }

    public static void applyUpdate(TipoEspecialidad entity, TipoEspecialidadUpdateDTO dto) {
        if (dto.getNombre() != null) {
            entity.setNombre(dto.getNombre());
        }
    }

    public static TipoEspecialidadResponseDTO toDTO(TipoEspecialidad entity) {
        if (entity == null) return null;
        return TipoEspecialidadResponseDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }
}
