package com.babytrackmaster.api_crecimiento.mapper;

import java.util.Date;

import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoRequest;
import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoResponse;
import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;

public class TipoCrecimientoMapper {

    public static TipoCrecimiento toEntity(TipoCrecimientoRequest req) {
        TipoCrecimiento t = new TipoCrecimiento();
        t.setNombre(req.getNombre());
        Date now = new Date();
        t.setCreatedAt(now);
        t.setUpdatedAt(now);
        return t;
    }

    public static void copyToEntity(TipoCrecimientoRequest req, TipoCrecimiento t) {
        t.setNombre(req.getNombre());
        t.setUpdatedAt(new Date());
    }

    public static TipoCrecimientoResponse toResponse(TipoCrecimiento t) {
        TipoCrecimientoResponse r = new TipoCrecimientoResponse();
        r.setId(t.getId());
        r.setNombre(t.getNombre());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());
        return r;
    }
}
