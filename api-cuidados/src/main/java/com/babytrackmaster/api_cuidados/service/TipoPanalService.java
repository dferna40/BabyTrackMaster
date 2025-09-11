package com.babytrackmaster.api_cuidados.service;

import java.util.List;

import com.babytrackmaster.api_cuidados.dto.TipoPanalRequest;
import com.babytrackmaster.api_cuidados.dto.TipoPanalResponse;

public interface TipoPanalService {
    TipoPanalResponse crear(TipoPanalRequest request);
    TipoPanalResponse actualizar(Long id, TipoPanalRequest request);
    void eliminar(Long id);
    TipoPanalResponse obtener(Long id);
    List<TipoPanalResponse> listar();
}

