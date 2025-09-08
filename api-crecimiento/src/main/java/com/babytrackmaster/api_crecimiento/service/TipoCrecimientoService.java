package com.babytrackmaster.api_crecimiento.service;

import java.util.List;

import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoRequest;
import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoResponse;

public interface TipoCrecimientoService {
    TipoCrecimientoResponse crear(TipoCrecimientoRequest request);
    TipoCrecimientoResponse actualizar(Long id, TipoCrecimientoRequest request);
    void eliminar(Long id);
    TipoCrecimientoResponse obtener(Long id);
    List<TipoCrecimientoResponse> listar();
}
