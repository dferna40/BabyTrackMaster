package com.babytrackmaster.api_alimentacion.mapper;

import java.util.Date;

import com.babytrackmaster.api_alimentacion.dto.AlimentacionRequest;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionResponse;
import com.babytrackmaster.api_alimentacion.entity.Alimentacion;

public class AlimentacionMapper {

    public static Alimentacion toEntity(AlimentacionRequest req, Long usuarioId, Long bebeId) {
        Alimentacion a = new Alimentacion();
        a.setUsuarioId(usuarioId);
        a.setBebeId(bebeId);
        a.setTipo(req.getTipo());
        a.setFechaHora(req.getFechaHora());
        a.setLado(req.getLado());
        a.setDuracionMin(req.getDuracionMin());
        a.setTipoLeche(req.getTipoLeche());
        a.setCantidadMl(req.getCantidadMl());
        a.setAlimento(req.getAlimento());
        a.setCantidad(req.getCantidad());
        a.setObservaciones(req.getObservaciones());
        Date now = new Date();
        a.setCreatedAt(now);
        a.setUpdatedAt(now);
        a.setEliminado(false);
        return a;
    }

    public static void copyToEntity(AlimentacionRequest req, Alimentacion a) {
        a.setTipo(req.getTipo());
        a.setFechaHora(req.getFechaHora());
        a.setLado(req.getLado());
        a.setDuracionMin(req.getDuracionMin());
        a.setTipoLeche(req.getTipoLeche());
        a.setCantidadMl(req.getCantidadMl());
        a.setAlimento(req.getAlimento());
        a.setCantidad(req.getCantidad());
        a.setObservaciones(req.getObservaciones());
        a.setUpdatedAt(new Date());
    }

    public static AlimentacionResponse toResponse(Alimentacion a) {
        AlimentacionResponse r = new AlimentacionResponse();
        r.setId(a.getId());
        r.setUsuarioId(a.getUsuarioId());
        r.setBebeId(a.getBebeId());
        r.setTipo(a.getTipo());
        r.setFechaHora(a.getFechaHora());
        r.setLado(a.getLado());
        r.setDuracionMin(a.getDuracionMin());
        r.setTipoLeche(a.getTipoLeche());
        r.setCantidadMl(a.getCantidadMl());
        r.setAlimento(a.getAlimento());
        r.setCantidad(a.getCantidad());
        r.setObservaciones(a.getObservaciones());
        r.setCreatedAt(a.getCreatedAt());
        r.setUpdatedAt(a.getUpdatedAt());
        return r;
    }
}
