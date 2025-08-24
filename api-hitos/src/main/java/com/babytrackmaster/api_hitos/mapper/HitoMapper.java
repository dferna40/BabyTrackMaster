package com.babytrackmaster.api_hitos.mapper;

import com.babytrackmaster.api_hitos.dto.HitoRequest;
import com.babytrackmaster.api_hitos.dto.HitoResponse;
import com.babytrackmaster.api_hitos.entity.Hito;

public class HitoMapper {

    public static Hito toEntity(HitoRequest req, Long usuarioId) {
        Hito h = new Hito();
        h.setUsuarioId(usuarioId);
        h.setBebeId(req.getBebeId());
        h.setTitulo(req.getTitulo());
        h.setFecha(req.getFecha());
        h.setDescripcion(req.getDescripcion());
        h.setImagenUrl(req.getImagenUrl());
        return h;
    }

    public static void updateEntity(Hito h, HitoRequest req) {
        h.setBebeId(req.getBebeId());
        h.setTitulo(req.getTitulo());
        h.setFecha(req.getFecha());
        h.setDescripcion(req.getDescripcion());
        h.setImagenUrl(req.getImagenUrl());
    }

    public static HitoResponse toResponse(Hito h) {
        HitoResponse r = new HitoResponse();
        r.setId(h.getId());
        r.setBebeId(h.getBebeId());
        r.setTitulo(h.getTitulo());
        r.setFecha(h.getFecha());
        r.setDescripcion(h.getDescripcion());
        r.setImagenUrl(h.getImagenUrl());
        return r;
    }
}
