package com.babytrackmaster.api_rutinas.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.babytrackmaster.api_rutinas.dto.RutinaCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionDTO;

public interface RutinaService {

    RutinaDTO crear(Long usuarioId, Long bebeId, RutinaCreateDTO dto);

    RutinaDTO obtener(Long usuarioId, Long bebeId, Long id);

    RutinaDTO actualizar(Long usuarioId, Long bebeId, Long id, RutinaCreateDTO dto);

    void eliminar(Long usuarioId, Long bebeId, Long id);

    Page<RutinaDTO> listar(Long usuarioId, Long bebeId, Boolean activo, String dia, Pageable pageable);

    RutinaDTO activar(Long usuarioId, Long bebeId, Long id);

    RutinaDTO desactivar(Long usuarioId, Long bebeId, Long id);

    RutinaEjecucionDTO registrarEjecucion(Long usuarioId, Long bebeId, Long rutinaId, RutinaEjecucionCreateDTO dto);

    java.util.List<RutinaEjecucionDTO> historial(Long usuarioId, Long bebeId, Long rutinaId, LocalDate desde, LocalDate hasta);
}
