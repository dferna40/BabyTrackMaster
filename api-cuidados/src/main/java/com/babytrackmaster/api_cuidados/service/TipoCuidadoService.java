package com.babytrackmaster.api_cuidados.service;

import java.util.List;

import com.babytrackmaster.api_cuidados.dto.TipoCuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.TipoCuidadoResponse;

public interface TipoCuidadoService {
    TipoCuidadoResponse crear(TipoCuidadoRequest request);
    TipoCuidadoResponse actualizar(Long id, TipoCuidadoRequest request);
    void eliminar(Long id);
    TipoCuidadoResponse obtener(Long id);
    List<TipoCuidadoResponse> listar();
}
