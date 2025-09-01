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
        return b;
    }

    public static BebeResponse toResponse(Bebe entity) {
        if (entity == null) return null;
        BebeResponse resp = new BebeResponse();
        resp.setId(entity.getId());
        resp.setNombre(entity.getNombre());
        resp.setFechaNacimiento(entity.getFechaNacimiento());
        resp.setSexo(entity.getSexo());
        return resp;
    }
}
