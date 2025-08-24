package com.babytrackmaster.api_hitos.service;

import java.time.YearMonth;
import java.util.List;

import com.babytrackmaster.api_hitos.dto.HitoRequest;
import com.babytrackmaster.api_hitos.dto.HitoResponse;

public interface HitoService {
    HitoResponse crear(Long usuarioId, HitoRequest request);
    HitoResponse actualizar(Long usuarioId, Long id, HitoRequest request);
    void eliminar(Long usuarioId, Long id);
    HitoResponse obtener(Long usuarioId, Long id);
    List<HitoResponse> listar(Long usuarioId);
    List<HitoResponse> listarPorBebe(Long usuarioId, Long bebeId);
    List<HitoResponse> listarPorMes(Long usuarioId, YearMonth mes);
    List<HitoResponse> buscarPorTitulo(Long usuarioId, String texto);
}