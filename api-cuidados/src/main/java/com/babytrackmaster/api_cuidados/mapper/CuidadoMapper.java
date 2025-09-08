package com.babytrackmaster.api_cuidados.mapper;

import java.util.Date;

import com.babytrackmaster.api_cuidados.dto.CuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;
import com.babytrackmaster.api_cuidados.entity.Cuidado;
import com.babytrackmaster.api_cuidados.entity.TipoCuidado;
import com.babytrackmaster.api_cuidados.repository.TipoCuidadoRepository;

public class CuidadoMapper {

    public static Cuidado toEntity(CuidadoRequest req, Long usuarioId, TipoCuidadoRepository tipoRepo) {
        Cuidado c = new Cuidado();
        c.setBebeId(req.getBebeId());
        c.setUsuarioId(usuarioId);
        TipoCuidado tipo = tipoRepo.findById(req.getTipoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de cuidado no encontrado: " + req.getTipoId()));
        c.setTipo(tipo);
        c.setInicio(req.getInicio());
        c.setFin(req.getFin());
        c.setDuracion(req.getDuracion());
        c.setCantidadMl(req.getCantidadMl());
        c.setTipoPanal(req.getTipoPanal());
        c.setMedicamento(req.getMedicamento());
        c.setDosis(req.getDosis());
        c.setObservaciones(req.getObservaciones());
        Date now = new Date();
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        c.setEliminado(false);
        return c;
    }

    public static void copyToEntity(CuidadoRequest req, Cuidado c, Long usuarioId, TipoCuidadoRepository tipoRepo) {
        c.setBebeId(req.getBebeId());
        c.setUsuarioId(usuarioId);
        TipoCuidado tipo = tipoRepo.findById(req.getTipoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de cuidado no encontrado: " + req.getTipoId()));
        c.setTipo(tipo);
        c.setInicio(req.getInicio());
        c.setFin(req.getFin());
        c.setDuracion(req.getDuracion());
        c.setCantidadMl(req.getCantidadMl());
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
        if (c.getTipo() != null) {
            r.setTipoId(c.getTipo().getId());
            r.setTipoNombre(c.getTipo().getNombre());
        }
        r.setInicio(c.getInicio());
        r.setFin(c.getFin());
        r.setDuracion(c.getDuracion());
        r.setCantidadMl(c.getCantidadMl());
        r.setTipoPanal(c.getTipoPanal());
        r.setMedicamento(c.getMedicamento());
        r.setDosis(c.getDosis());
        r.setObservaciones(c.getObservaciones());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        return r;
    }
}
