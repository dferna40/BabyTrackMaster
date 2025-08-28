package com.babytrackmaster.api_cuidados.mapper;

import java.util.Date;

import com.babytrackmaster.api_cuidados.dto.TipoCuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.TipoCuidadoResponse;
import com.babytrackmaster.api_cuidados.entity.TipoCuidado;

public class TipoCuidadoMapper {

    public static TipoCuidado toEntity(TipoCuidadoRequest req) {
        TipoCuidado t = new TipoCuidado();
        t.setNombre(req.getNombre());
        Date now = new Date();
        t.setCreatedAt(now);
        t.setUpdatedAt(now);
        return t;
    }

    public static void copyToEntity(TipoCuidadoRequest req, TipoCuidado t) {
        t.setNombre(req.getNombre());
        t.setUpdatedAt(new Date());
    }

    public static TipoCuidadoResponse toResponse(TipoCuidado t) {
        TipoCuidadoResponse r = new TipoCuidadoResponse();
        r.setId(t.getId());
        r.setNombre(t.getNombre());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());
        return r;
    }
}
