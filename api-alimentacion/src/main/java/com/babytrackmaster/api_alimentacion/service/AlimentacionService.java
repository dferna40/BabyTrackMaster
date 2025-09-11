package com.babytrackmaster.api_alimentacion.service;

import java.util.List;

import com.babytrackmaster.api_alimentacion.dto.AlimentacionRequest;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionResponse;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionStatsResponse;
import com.babytrackmaster.api_alimentacion.entity.TipoLactancia;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoLecheBiberon;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacionSolido;

public interface AlimentacionService {
    AlimentacionResponse crear(Long usuarioId, Long bebeId, AlimentacionRequest request);
    AlimentacionResponse actualizar(Long usuarioId, Long bebeId, Long id, AlimentacionRequest request);
    void eliminar(Long usuarioId, Long bebeId, Long id);
    AlimentacionResponse obtener(Long usuarioId, Long bebeId, Long id);
    List<AlimentacionResponse> listar(Long usuarioId, Long bebeId);
    AlimentacionStatsResponse stats(Long usuarioId, Long bebeId, Long tipoAlimentacionId);
    List<TipoLactancia> listarTiposLactancia();
    List<TipoAlimentacion> listarTiposAlimentacion();
    List<TipoLecheBiberon> listarTiposBiberon();
    List<TipoAlimentacionSolido> listarTiposAlimentacionSolido();
}
