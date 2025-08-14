package com.babytrackmaster.api_cuidados.mapper;

import java.util.Date;

import com.babytrackmaster.api_cuidados.dto.CuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;
import com.babytrackmaster.api_cuidados.entity.Cuidado;

public class CuidadoMapper {

	public static Cuidado toEntity(CuidadoRequest req) {
        Cuidado c = new Cuidado();
        c.setBebeId(req.getBebeId());
        c.setUsuarioId(req.getUsuarioId());
        c.setTipo(req.getTipo().name());
        c.setInicio(req.getInicio());
        c.setFin(req.getFin());
        c.setCantidadMl(req.getCantidadMl());
        c.setPecho(req.getPecho());
        c.setTipoPanal(req.getTipoPanal());
        c.setMedicamento(req.getMedicamento());
        c.setDosis(req.getDosis());
        c.setObservaciones(req.getObservaciones());
        Date now = new Date();
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        return c;
    }

    public static void copyToEntity(CuidadoRequest req, Cuidado c) {
        c.setBebeId(req.getBebeId());
        c.setUsuarioId(req.getUsuarioId());
        c.setTipo(req.getTipo().name());
        c.setInicio(req.getInicio());
        c.setFin(req.getFin());
        c.setCantidadMl(req.getCantidadMl());
        c.setPecho(req.getPecho());
        c.setTipoPanal(req.getTipoPanal());
        c.setMedicamento(req.getMedicamento());
        c.setDosis(req.getDosis());
        c.setObservaciones(req.getObservaciones());
        c.setUpdatedAt(new Date());
    }

    public static CuidadoResponse toResponse(Cuidado c) {
        CuidadoResponse r = new CuidadoResponse();
        r.setId(c.getId());
        r.setBebeId(c.getBebeId());
        r.setUsuarioId(c.getUsuarioId());
        r.setTipo(c.getTipo());
        r.setInicio(c.getInicio());
        r.setFin(c.getFin());
        r.setCantidadMl(c.getCantidadMl());
        r.setPecho(c.getPecho());
        r.setTipoPanal(c.getTipoPanal());
        r.setMedicamento(c.getMedicamento());
        r.setDosis(c.getDosis());
        r.setObservaciones(c.getObservaciones());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        return r;
    }
}
