package com.babytrackmaster.api_diario.service;

import com.babytrackmaster.api_diario.dto.DiarioCreateDTO;
import com.babytrackmaster.api_diario.dto.DiarioResponseDTO;
import com.babytrackmaster.api_diario.dto.DiarioUpdateDTO;
import com.babytrackmaster.api_diario.dto.PageResponseDTO;
import java.util.List;

public interface DiarioService {
    DiarioResponseDTO crear(Long usuarioId, DiarioCreateDTO dto);
    DiarioResponseDTO obtener(Long usuarioId, Long id);
    DiarioResponseDTO actualizar(Long usuarioId, Long id, DiarioUpdateDTO dto);
    void eliminar(Long usuarioId, Long id);
    PageResponseDTO<DiarioResponseDTO> listarRango(Long usuarioId, String desde, String hasta, int page, int size);
    PageResponseDTO<DiarioResponseDTO> listarDia(Long usuarioId, String fecha, int page, int size);
    PageResponseDTO<DiarioResponseDTO> listarPorTag(Long usuarioId, String tag, int page, int size);
    List<DiarioResponseDTO> listarPorBebe(Long usuarioId, Long bebeId);
}
