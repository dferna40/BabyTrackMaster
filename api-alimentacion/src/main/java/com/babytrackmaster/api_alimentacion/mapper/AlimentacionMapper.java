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
        Date now = new Date();
        a.setFechaHora(req.getFechaHora() != null ? req.getFechaHora() : now);
        a.setLado(req.getLado());
        a.setDuracionMin(req.getDuracionMin());
        a.setTipoLactancia(req.getTipoLactancia());
        a.setTipoLeche(req.getTipoLeche());
        a.setCantidadMl(req.getCantidadMl());
        a.setCantidadLecheFormula(req.getCantidadLecheFormula());
        a.setAlimento(req.getAlimento());
        a.setCantidad(req.getCantidad());
        a.setCantidadOtrosAlimentos(req.getCantidadOtrosAlimentos());
        a.setObservaciones(req.getObservaciones());
        a.setCreatedAt(now);
        a.setUpdatedAt(now);
        a.setEliminado(false);
        return a;
    }

    public static void copyToEntity(AlimentacionRequest req, Alimentacion a) {
        a.setTipo(req.getTipo());
        a.setFechaHora(req.getFechaHora() != null ? req.getFechaHora() : a.getFechaHora());
        a.setLado(req.getLado());
        a.setDuracionMin(req.getDuracionMin());
        a.setTipoLactancia(req.getTipoLactancia());
        a.setTipoLeche(req.getTipoLeche());
        a.setCantidadMl(req.getCantidadMl());
        a.setCantidadLecheFormula(req.getCantidadLecheFormula());
        a.setAlimento(req.getAlimento());
        a.setCantidad(req.getCantidad());
        a.setCantidadOtrosAlimentos(req.getCantidadOtrosAlimentos());
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
        r.setTipoLactancia(a.getTipoLactancia());
        r.setTipoLeche(a.getTipoLeche());
        r.setCantidadMl(a.getCantidadMl());
        r.setCantidadLecheFormula(a.getCantidadLecheFormula());
        r.setAlimento(a.getAlimento());
        r.setCantidad(a.getCantidad());
        r.setCantidadOtrosAlimentos(a.getCantidadOtrosAlimentos());
        r.setObservaciones(a.getObservaciones());
        r.setCreatedAt(a.getCreatedAt());
        r.setUpdatedAt(a.getUpdatedAt());
        return r;
    }
}
