package com.babytrackmaster.api_bebe.mapper;

import com.babytrackmaster.api_bebe.dto.TipoLactanciaResponse;
import com.babytrackmaster.api_bebe.entity.TipoLactancia;

public class TipoLactanciaMapper {
    public static TipoLactanciaResponse toResponse(TipoLactancia entity) {
        if (entity == null) return null;
        TipoLactanciaResponse resp = new TipoLactanciaResponse();
        resp.setId(entity.getId());
        resp.setNombre(entity.getNombre());
        return resp;
        }
}
