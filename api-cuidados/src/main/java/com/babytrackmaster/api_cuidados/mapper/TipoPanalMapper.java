package com.babytrackmaster.api_cuidados.mapper;

import java.util.Date;

import com.babytrackmaster.api_cuidados.dto.TipoPanalRequest;
import com.babytrackmaster.api_cuidados.dto.TipoPanalResponse;
import com.babytrackmaster.api_cuidados.entity.TipoPanal;

public class TipoPanalMapper {

    public static TipoPanal toEntity(TipoPanalRequest req) {
        TipoPanal t = new TipoPanal();
        t.setNombre(req.getNombre());
        Date now = new Date();
        t.setCreatedAt(now);
        t.setUpdatedAt(now);
        return t;
    }

    public static void copyToEntity(TipoPanalRequest req, TipoPanal t) {
        t.setNombre(req.getNombre());
        t.setUpdatedAt(new Date());
    }

    public static TipoPanalResponse toResponse(TipoPanal t) {
        TipoPanalResponse r = new TipoPanalResponse();
        r.setId(t.getId());
        r.setNombre(t.getNombre());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());
        return r;
    }
}

