package com.babytrackmaster.api_bebe.service;

import java.util.List;

import com.babytrackmaster.api_bebe.dto.BebeRequest;
import com.babytrackmaster.api_bebe.dto.BebeResponse;

public interface BebeService {
    BebeResponse crear(Long usuarioId, BebeRequest request);
    BebeResponse actualizar(Long usuarioId, Long id, BebeRequest request);
    void eliminar(Long usuarioId, Long id);
    BebeResponse obtener(Long usuarioId, Long id);
    List<BebeResponse> listar(Long usuarioId);
}
