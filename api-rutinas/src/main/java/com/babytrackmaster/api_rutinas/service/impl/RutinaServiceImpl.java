package com.babytrackmaster.api_rutinas.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.babytrackmaster.api_rutinas.dto.RutinaCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionDTO;
import com.babytrackmaster.api_rutinas.entity.Rutina;
import com.babytrackmaster.api_rutinas.entity.RutinaEjecucion;
import com.babytrackmaster.api_rutinas.exception.NotFoundException;
import com.babytrackmaster.api_rutinas.mapper.RutinaEjecucionMapper;
import com.babytrackmaster.api_rutinas.mapper.RutinaMapper;
import com.babytrackmaster.api_rutinas.repository.RutinaEjecucionRepository;
import com.babytrackmaster.api_rutinas.repository.RutinaRepository;
import com.babytrackmaster.api_rutinas.service.RutinaService;

@Service
public class RutinaServiceImpl implements RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private RutinaEjecucionRepository ejecucionRepository;

    public RutinaDTO crear(Long usuarioId, Long bebeId, RutinaCreateDTO dto) {
        Rutina r = RutinaMapper.toEntity(dto, usuarioId, bebeId);
        r = rutinaRepository.save(r);
        return RutinaMapper.toDTO(r);
    }

    public RutinaDTO obtener(Long usuarioId, Long bebeId, Long id) {
        Rutina r = rutinaRepository.findOneByIdAndUsuarioAndBebe(id, usuarioId, bebeId);
        if (r == null || Boolean.TRUE.equals(r.getEliminado())) throw new NotFoundException("Rutina no encontrada");
        return RutinaMapper.toDTO(r);
    }

    public RutinaDTO actualizar(Long usuarioId, Long bebeId, Long id, RutinaCreateDTO dto) {
        Rutina r = rutinaRepository.findOneByIdAndUsuarioAndBebe(id, usuarioId, bebeId);
        if (r == null || Boolean.TRUE.equals(r.getEliminado())) throw new NotFoundException("Rutina no encontrada");
        RutinaMapper.updateEntity(r, dto);
        r = rutinaRepository.save(r);
        return RutinaMapper.toDTO(r);
    }

    public void eliminar(Long usuarioId, Long bebeId, Long id) {
        Rutina r = rutinaRepository.findOneByIdAndUsuarioAndBebe(id, usuarioId, bebeId);
        if (r == null || Boolean.TRUE.equals(r.getEliminado())) throw new NotFoundException("Rutina no encontrada");
        r.setEliminado(Boolean.TRUE);
        rutinaRepository.save(r);
    }

    public Page<RutinaDTO> listar(Long usuarioId, Long bebeId, Boolean activo, String dia, Pageable pageable) {
        Page<Rutina> page = rutinaRepository.findAllByUsuarioAndBebeAndFilters(usuarioId, bebeId, activo, dia, pageable);
        List<RutinaDTO> lista = new ArrayList<RutinaDTO>();
        for (Rutina r : page.getContent()) {
            lista.add(RutinaMapper.toDTO(r));
        }
        return new PageImpl<RutinaDTO>(lista, pageable, page.getTotalElements());
    }

    public RutinaDTO activar(Long usuarioId, Long bebeId, Long id) {
        Rutina r = rutinaRepository.findOneByIdAndUsuarioAndBebe(id, usuarioId, bebeId);
        if (r == null || Boolean.TRUE.equals(r.getEliminado())) throw new NotFoundException("Rutina no encontrada");
        r.setActiva(Boolean.TRUE);
        r = rutinaRepository.save(r);
        return RutinaMapper.toDTO(r);
    }

    public RutinaDTO desactivar(Long usuarioId, Long bebeId, Long id) {
        Rutina r = rutinaRepository.findOneByIdAndUsuarioAndBebe(id, usuarioId, bebeId);
        if (r == null || Boolean.TRUE.equals(r.getEliminado())) throw new NotFoundException("Rutina no encontrada");
        r.setActiva(Boolean.FALSE);
        r = rutinaRepository.save(r);
        return RutinaMapper.toDTO(r);
    }

    public RutinaEjecucionDTO registrarEjecucion(Long usuarioId, Long bebeId, Long rutinaId, RutinaEjecucionCreateDTO dto) {
        Rutina r = rutinaRepository.findOneByIdAndUsuarioAndBebe(rutinaId, usuarioId, bebeId);
        if (r == null || Boolean.TRUE.equals(r.getEliminado())) throw new NotFoundException("Rutina no encontrada");
        RutinaEjecucion e = RutinaEjecucionMapper.toEntity(dto, r);
        e = ejecucionRepository.save(e);
        return RutinaEjecucionMapper.toDTO(e);
    }

    public List<RutinaEjecucionDTO> historial(Long usuarioId, Long bebeId, Long rutinaId, LocalDate desde, LocalDate hasta) {
        Rutina r = rutinaRepository.findOneByIdAndUsuarioAndBebe(rutinaId, usuarioId, bebeId);
        if (r == null || Boolean.TRUE.equals(r.getEliminado())) throw new NotFoundException("Rutina no encontrada");
        LocalDateTime d = desde != null ? desde.atStartOfDay() : null;
        LocalDateTime h = hasta != null ? hasta.atTime(23,59,59) : null;
        List<RutinaEjecucion> lista = ejecucionRepository.buscarHistorial(rutinaId, usuarioId, d, h);
        List<RutinaEjecucionDTO> res = new ArrayList<RutinaEjecucionDTO>();
        for (RutinaEjecucion e : lista) {
            res.add(RutinaEjecucionMapper.toDTO(e));
        }
        return res;
    }
}