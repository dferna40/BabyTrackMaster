package com.babytrackmaster.api_gastos.mapper;

import com.babytrackmaster.api_gastos.dto.GastoResponse;
import com.babytrackmaster.api_gastos.entity.Gasto;

public class GastoMapper {

    public static GastoResponse toResponse(Gasto g) {
        GastoResponse dto = new GastoResponse();
        dto.setId(g.getId());
        if (g.getCategoria() != null) {
            dto.setCategoriaId(g.getCategoria().getId());
            dto.setCategoriaNombre(g.getCategoria().getNombre());
        }
        dto.setCantidad(g.getCantidad());
        dto.setFecha(g.getFecha());
        dto.setDescripcion(g.getDescripcion());
        dto.setBebeId(g.getBebeId());
        return dto;
    }
}
