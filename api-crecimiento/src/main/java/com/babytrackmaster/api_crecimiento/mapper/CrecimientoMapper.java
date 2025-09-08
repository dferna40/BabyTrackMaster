package com.babytrackmaster.api_crecimiento.mapper;

import java.util.Date;

import com.babytrackmaster.api_crecimiento.dto.CrecimientoRequest;
import com.babytrackmaster.api_crecimiento.dto.CrecimientoResponse;
import com.babytrackmaster.api_crecimiento.entity.Crecimiento;
import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;
import com.babytrackmaster.api_crecimiento.repository.TipoCrecimientoRepository;

public class CrecimientoMapper {

    public static Crecimiento toEntity(CrecimientoRequest req, Long usuarioId, TipoCrecimientoRepository tipoRepo) {
        Crecimiento c = new Crecimiento();
        c.setBebeId(req.getBebeId());
        c.setUsuarioId(usuarioId);
        TipoCrecimiento tipo = tipoRepo.findById(req.getTipoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de crecimiento no encontrado: " + req.getTipoId()));
        c.setTipo(tipo);
        c.setFecha(req.getFecha());
        c.setValor(req.getValor());
        c.setUnidad(req.getUnidad());
        c.setObservaciones(req.getObservaciones());
        Date now = new Date();
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        c.setEliminado(false);
        return c;
    }

    public static void copyToEntity(CrecimientoRequest req, Crecimiento c, Long usuarioId, TipoCrecimientoRepository tipoRepo) {
        c.setBebeId(req.getBebeId());
        c.setUsuarioId(usuarioId);
        TipoCrecimiento tipo = tipoRepo.findById(req.getTipoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de crecimiento no encontrado: " + req.getTipoId()));
        c.setTipo(tipo);
        c.setFecha(req.getFecha());
        c.setValor(req.getValor());
        c.setUnidad(req.getUnidad());
        c.setObservaciones(req.getObservaciones());
        c.setUpdatedAt(new Date());
    }

    public static CrecimientoResponse toResponse(Crecimiento c) {
        CrecimientoResponse r = new CrecimientoResponse();
        r.setId(c.getId());
        r.setBebeId(c.getBebeId());
        r.setUsuarioId(c.getUsuarioId());
        if (c.getTipo() != null) {
            r.setTipoId(c.getTipo().getId());
            r.setTipoNombre(c.getTipo().getNombre());
        }
        r.setFecha(c.getFecha());
        r.setValor(c.getValor());
        r.setUnidad(c.getUnidad());
        r.setObservaciones(c.getObservaciones());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        return r;
    }
}
