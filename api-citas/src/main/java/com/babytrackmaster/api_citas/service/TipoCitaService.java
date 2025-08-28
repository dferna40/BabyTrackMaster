package com.babytrackmaster.api_citas.service;

import com.babytrackmaster.api_citas.dto.TipoCitaCreateDTO;
import com.babytrackmaster.api_citas.dto.TipoCitaResponseDTO;
import com.babytrackmaster.api_citas.dto.TipoCitaUpdateDTO;
import java.util.List;

public interface TipoCitaService {
    TipoCitaResponseDTO crear(TipoCitaCreateDTO dto);
    TipoCitaResponseDTO actualizar(Long id, TipoCitaUpdateDTO dto);
    void eliminar(Long id);
    TipoCitaResponseDTO obtener(Long id);
    List<TipoCitaResponseDTO> listarTodos();
}
