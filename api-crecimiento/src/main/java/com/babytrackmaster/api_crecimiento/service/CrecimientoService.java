package com.babytrackmaster.api_crecimiento.service;

import java.util.Date;
import java.util.List;

import com.babytrackmaster.api_crecimiento.dto.CrecimientoRequest;
import com.babytrackmaster.api_crecimiento.dto.CrecimientoResponse;
import com.babytrackmaster.api_crecimiento.dto.CrecimientoStatsResponse;

public interface CrecimientoService {
    CrecimientoResponse crear(Long usuarioId, CrecimientoRequest request);
    CrecimientoResponse actualizar(Long usuarioId, Long id, CrecimientoRequest request);
    void eliminar(Long usuarioId, Long id);
    CrecimientoResponse obtener(Long usuarioId, Long id);
    List<CrecimientoResponse> listarPorBebe(Long usuarioId, Long bebeId, Integer limit);
    List<CrecimientoResponse> listarPorBebeYTipo(Long usuarioId, Long bebeId, Long tipoId);
    List<CrecimientoResponse> listarPorRango(Long usuarioId, Long bebeId, Date desde, Date hasta);
    CrecimientoStatsResponse obtenerEstadisticas(Long usuarioId, Long bebeId, Long tipoId, Date desde, Date hasta);
}
