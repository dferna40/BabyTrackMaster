package com.babytrackmaster.api_citas.service;

import com.babytrackmaster.api_citas.dto.TipoEspecialidadCreateDTO;
import com.babytrackmaster.api_citas.dto.TipoEspecialidadResponseDTO;
import com.babytrackmaster.api_citas.dto.TipoEspecialidadUpdateDTO;
import java.util.List;

public interface TipoEspecialidadService {
    TipoEspecialidadResponseDTO crear(TipoEspecialidadCreateDTO dto);
    TipoEspecialidadResponseDTO actualizar(Long id, TipoEspecialidadUpdateDTO dto);
    void eliminar(Long id);
    TipoEspecialidadResponseDTO obtener(Long id);
    List<TipoEspecialidadResponseDTO> listarTodos();
}
