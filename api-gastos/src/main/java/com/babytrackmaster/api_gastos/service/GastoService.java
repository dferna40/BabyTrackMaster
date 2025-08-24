package com.babytrackmaster.api_gastos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.babytrackmaster.api_gastos.dto.GastoCreateRequest;
import com.babytrackmaster.api_gastos.dto.GastoResponse;
import com.babytrackmaster.api_gastos.dto.GastoUpdateRequest;
import com.babytrackmaster.api_gastos.dto.ResumenMensualResponse;

public interface GastoService {
    GastoResponse crear(Long usuarioIdDelToken, GastoCreateRequest req);
    GastoResponse actualizar(Long usuarioIdDelToken, Long id, GastoUpdateRequest req);
    void eliminar(Long usuarioIdDelToken, Long id);
    GastoResponse obtener(Long usuarioIdDelToken, Long id);

    Page<GastoResponse> listarPorMes(Long usuarioIdDelToken, int anio, int mes, Pageable pageable);
    Page<GastoResponse> listarPorCategoria(Long usuarioIdDelToken, Long categoriaId, Pageable pageable);

    ResumenMensualResponse resumenMensual(Long usuarioIdDelToken, int anio, int mes);
}