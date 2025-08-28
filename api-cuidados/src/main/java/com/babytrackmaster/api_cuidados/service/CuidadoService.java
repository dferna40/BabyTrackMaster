package com.babytrackmaster.api_cuidados.service;

import java.util.Date;
import java.util.List;

import com.babytrackmaster.api_cuidados.dto.CuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;

public interface CuidadoService {
    CuidadoResponse crear(CuidadoRequest request);
    CuidadoResponse actualizar(Long id, CuidadoRequest request);
    void eliminar(Long id);
    CuidadoResponse obtener(Long id);
    List<CuidadoResponse> listarPorBebe(Long bebeId);
    List<CuidadoResponse> listarPorBebeYTipo(Long bebeId, Long tipoId);
    List<CuidadoResponse> listarPorRango(Long bebeId, Date desde, Date hasta);
}
