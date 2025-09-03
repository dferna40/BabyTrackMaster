package com.babytrackmaster.api_cuidados.service;

import java.util.Date;
import java.util.List;

import com.babytrackmaster.api_cuidados.dto.CuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;
import com.babytrackmaster.api_cuidados.dto.QuickStatsResponse;

public interface CuidadoService {
    CuidadoResponse crear(Long usuarioId, CuidadoRequest request);
    CuidadoResponse actualizar(Long usuarioId, Long id, CuidadoRequest request);
    void eliminar(Long usuarioId, Long id);
    CuidadoResponse obtener(Long usuarioId, Long id);
    List<CuidadoResponse> listarPorBebe(Long usuarioId, Long bebeId, Integer limit);
    List<CuidadoResponse> listarPorBebeYTipo(Long usuarioId, Long bebeId, Long tipoId);
    List<CuidadoResponse> listarPorRango(Long usuarioId, Long bebeId, Date desde, Date hasta);
    QuickStatsResponse obtenerEstadisticasRapidas(Long usuarioId, Long bebeId, Date fecha);
}
