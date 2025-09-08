package com.babytrackmaster.api_crecimiento.mapper;

import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoRequest;
import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoResponse;
import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;

public class TipoCrecimientoMapper {

    public static TipoCrecimiento toEntity(TipoCrecimientoRequest req) {
        TipoCrecimiento t = new TipoCrecimiento();
        t.setNombre(req.getNombre());
        return t;
    }

    public static void copyToEntity(TipoCrecimientoRequest req, TipoCrecimiento t) {
        t.setNombre(req.getNombre());
    }

    public static TipoCrecimientoResponse toResponse(TipoCrecimiento t) {
        TipoCrecimientoResponse r = new TipoCrecimientoResponse();
        r.setId(t.getId());
        r.setNombre(t.getNombre());
        return r;
    }
}
