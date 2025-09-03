package com.babytrackmaster.api_citas.service;

import com.babytrackmaster.api_citas.dto.*;
import org.springframework.data.domain.Page;

public interface CitaService {
    CitaResponseDTO crear(CitaCreateDTO dto, Long usuarioId);
    CitaResponseDTO actualizar(Long id, Long usuarioId, CitaUpdateDTO dto);
    CitaResponseDTO confirmar(Long id, Long usuarioId);
    CitaResponseDTO completar(Long id, Long usuarioId);
    CitaResponseDTO noAsistida(Long id, Long usuarioId);
    void eliminar(Long id, Long usuarioId);
    CitaResponseDTO obtener(Long id, Long usuarioId);
    Page<CitaResponseDTO> listarRango(Long usuarioId, String desde, String hasta, int page, int size);
    Page<CitaResponseDTO> listarPorEstado(Long usuarioId, Long estadoId, int page, int size);
    Page<CitaResponseDTO> listarPorTipo(Long usuarioId, Long tipoId, int page, int size);
    Page<CitaResponseDTO> listarPorMedico(Long usuarioId, String medico, int page, int size);
    Page<CitaResponseDTO> listarPorBebe(Long usuarioId, Long bebeId, int page, int size);
}
