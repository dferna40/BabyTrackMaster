package com.babytrackmaster.api_bebe.mapper;

import com.babytrackmaster.api_bebe.dto.BebeRequest;
import com.babytrackmaster.api_bebe.dto.BebeResponse;
import com.babytrackmaster.api_bebe.entity.Bebe;

public class BebeMapper {

    public static Bebe toEntity(BebeRequest req) {
        if (req == null) return null;
        Bebe b = new Bebe();
        b.setNombre(req.getNombre());
        b.setFechaNacimiento(req.getFechaNacimiento());
        b.setSexo(req.getSexo());
        b.setPesoNacer(req.getPesoNacer());
        b.setTallaNacer(req.getTallaNacer());
        b.setSemanasGestacion(req.getSemanasGestacion());
        b.setPesoActual(req.getPesoActual());
        b.setTallaActual(req.getTallaActual());
        if (req.getBebeActivo() != null) {
            b.setBebeActivo(req.getBebeActivo());
        }
        b.setNumeroSs(req.getNumeroSs());
        b.setGrupoSanguineo(req.getGrupoSanguineo());
        b.setMedicaciones(req.getMedicaciones());
        b.setAlergias(req.getAlergias());
        b.setPediatra(req.getPediatra());
        b.setCentroMedico(req.getCentroMedico());
        b.setTelefonoCentroMedico(req.getTelefonoCentroMedico());
        b.setObservaciones(req.getObservaciones());
        b.setImagenBebe(req.getImagenBebe());
        return b;
    }

    public static BebeResponse toResponse(Bebe entity) {
        if (entity == null) return null;
        BebeResponse resp = new BebeResponse();
        resp.setId(entity.getId());
        resp.setNombre(entity.getNombre());
        resp.setFechaNacimiento(entity.getFechaNacimiento());
        resp.setSexo(entity.getSexo());
        resp.setPesoNacer(entity.getPesoNacer());
        resp.setTallaNacer(entity.getTallaNacer());
        resp.setSemanasGestacion(entity.getSemanasGestacion());
        resp.setPesoActual(entity.getPesoActual());
        resp.setTallaActual(entity.getTallaActual());
        resp.setBebeActivo(entity.getBebeActivo());
        resp.setNumeroSs(entity.getNumeroSs());
        resp.setGrupoSanguineo(entity.getGrupoSanguineo());
        resp.setMedicaciones(entity.getMedicaciones());
        resp.setAlergias(entity.getAlergias());
        resp.setPediatra(entity.getPediatra());
        resp.setCentroMedico(entity.getCentroMedico());
        resp.setTelefonoCentroMedico(entity.getTelefonoCentroMedico());
        resp.setObservaciones(entity.getObservaciones());
        resp.setImagenBebe(entity.getImagenBebe());
        return resp;
    }
}
