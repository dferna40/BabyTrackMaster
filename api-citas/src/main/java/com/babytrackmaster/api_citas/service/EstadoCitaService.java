package com.babytrackmaster.api_citas.service;

import com.babytrackmaster.api_citas.dto.EstadoCitaCreateDTO;
import com.babytrackmaster.api_citas.dto.EstadoCitaResponseDTO;
import com.babytrackmaster.api_citas.dto.EstadoCitaUpdateDTO;
import java.util.List;

public interface EstadoCitaService {
    EstadoCitaResponseDTO crear(EstadoCitaCreateDTO dto);
    EstadoCitaResponseDTO actualizar(Long id, EstadoCitaUpdateDTO dto);
    void eliminar(Long id);
    EstadoCitaResponseDTO obtener(Long id);
    List<EstadoCitaResponseDTO> listarTodos();
}

