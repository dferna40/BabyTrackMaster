package com.babytrackmaster.api_alimentacion.mapper;


import com.babytrackmaster.api_alimentacion.dto.AlimentacionRequest;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionResponse;
import com.babytrackmaster.api_alimentacion.entity.Alimentacion;

public class AlimentacionMapper {

    public static Alimentacion toEntity(AlimentacionRequest req, Long usuarioId, Long bebeId) {
        Alimentacion a = new Alimentacion();
        a.setUsuarioId(usuarioId);
        a.setBebeId(bebeId);
        a.setTipoAlimentacion(req.getTipoAlimentacion());
        a.setFechaHora(req.getFechaHora());
        a.setLado(req.getLado());
        a.setDuracionMin(req.getDuracionMin());
        a.setTipoLactancia(req.getTipoLactancia());
        a.setTipoBiberon(req.getTipoBiberon());
        a.setCantidadMl(req.getCantidadMl());
        a.setCantidadLecheFormula(req.getCantidadLecheFormula());
        a.setTipoAlimentacionSolido(req.getTipoAlimentacionSolido());
        a.setCantidadAlimentoSolido(req.getCantidadAlimentoSolido());
        a.setCantidad(req.getCantidad());
        a.setCantidadOtrosAlimentos(req.getCantidadOtrosAlimentos());
        a.setAlimentacionOtros(req.getAlimentacionOtros());
        a.setObservaciones(req.getObservaciones());
        a.setEliminado(false);
        return a;
    }

    public static void copyToEntity(AlimentacionRequest req, Alimentacion a) {
        a.setTipoAlimentacion(req.getTipoAlimentacion());
        if (req.getFechaHora() != null) {
            a.setFechaHora(req.getFechaHora());
        }
        a.setLado(req.getLado());
        a.setDuracionMin(req.getDuracionMin());
        a.setTipoLactancia(req.getTipoLactancia());
        a.setTipoBiberon(req.getTipoBiberon());
        a.setCantidadMl(req.getCantidadMl());
        a.setCantidadLecheFormula(req.getCantidadLecheFormula());
        a.setTipoAlimentacionSolido(req.getTipoAlimentacionSolido());
        a.setCantidadAlimentoSolido(req.getCantidadAlimentoSolido());
        a.setCantidad(req.getCantidad());
        a.setCantidadOtrosAlimentos(req.getCantidadOtrosAlimentos());
        a.setAlimentacionOtros(req.getAlimentacionOtros());
        a.setObservaciones(req.getObservaciones());
    }

    public static AlimentacionResponse toResponse(Alimentacion a) {
        AlimentacionResponse r = new AlimentacionResponse();
        r.setId(a.getId());
        r.setUsuarioId(a.getUsuarioId());
        r.setBebeId(a.getBebeId());
        r.setTipoAlimentacion(a.getTipoAlimentacion());
        r.setFechaHora(a.getFechaHora());
        r.setLado(a.getLado());
        r.setDuracionMin(a.getDuracionMin());
        r.setTipoLactancia(a.getTipoLactancia());
        r.setTipoBiberon(a.getTipoBiberon());
        r.setCantidadMl(a.getCantidadMl());
        r.setCantidadLecheFormula(a.getCantidadLecheFormula());
        r.setTipoAlimentacionSolido(a.getTipoAlimentacionSolido());
        r.setCantidadAlimentoSolido(a.getCantidadAlimentoSolido());
        r.setCantidad(a.getCantidad());
        r.setCantidadOtrosAlimentos(a.getCantidadOtrosAlimentos());
        r.setAlimentacionOtros(a.getAlimentacionOtros());
        r.setObservaciones(a.getObservaciones());
        r.setCreatedAt(a.getCreatedAt());
        r.setUpdatedAt(a.getUpdatedAt());
        return r;
    }
}
