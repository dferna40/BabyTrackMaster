package com.babytrackmaster.api_gastos.mapper;

import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
import com.babytrackmaster.api_gastos.entity.CategoriaGasto;

public class CategoriaMapper {

    public static CategoriaResponse toResponse(CategoriaGasto c) {
        CategoriaResponse dto = new CategoriaResponse();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        return dto;
    }
}
