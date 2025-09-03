package com.babytrackmaster.api_citas.service.impl;

import com.babytrackmaster.api_citas.dto.*;
import com.babytrackmaster.api_citas.entity.Cita;
import com.babytrackmaster.api_citas.entity.TipoCita;
import com.babytrackmaster.api_citas.entity.EstadoCita;
import com.babytrackmaster.api_citas.exception.NotFoundException;
import com.babytrackmaster.api_citas.mapper.CitaMapper;
import com.babytrackmaster.api_citas.repository.CitaRepository;
import com.babytrackmaster.api_citas.repository.TipoCitaRepository;
import com.babytrackmaster.api_citas.repository.EstadoCitaRepository;
import com.babytrackmaster.api_citas.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository repo;
    private final TipoCitaRepository tipoRepo;
    private final EstadoCitaRepository estadoRepo;

    public CitaResponseDTO crear(CitaCreateDTO dto, Long usuarioId) {
        TipoCita tipo = tipoRepo.findById(dto.getTipoId())
                .orElseThrow(() -> new NotFoundException("Tipo de cita no encontrado"));
        EstadoCita estado = estadoRepo.findByNombreIgnoreCase("Programada")
                .orElseThrow(() -> new NotFoundException("Estado de cita por defecto no encontrado"));
        Cita c = CitaMapper.toEntity(dto, usuarioId, tipo, estado);
        c = repo.save(c);
        return CitaMapper.toDTO(c);
    }

    public CitaResponseDTO actualizar(Long id, Long usuarioId, CitaUpdateDTO dto) {
        Cita c = repo.findOneByIdAndUsuario(id, usuarioId);
        if (c == null) {
            throw new NotFoundException("Cita no encontrada");
        }
        TipoCita tipo = null;
        if (dto.getTipoId() != null) {
            tipo = tipoRepo.findById(dto.getTipoId())
                    .orElseThrow(() -> new NotFoundException("Tipo de cita no encontrado"));
        }
        EstadoCita estado = null;
        if (dto.getEstadoId() != null) {
            estado = estadoRepo.findById(dto.getEstadoId())
                    .orElseThrow(() -> new NotFoundException("Estado de cita no encontrado"));
        }
        CitaMapper.applyUpdate(c, dto, tipo, estado);
        c = repo.save(c);
        return CitaMapper.toDTO(c);
    }

    public void eliminar(Long id, Long usuarioId) {
        Cita c = repo.findOneByIdAndUsuario(id, usuarioId);
        if (c == null) {
            throw new NotFoundException("Cita no encontrada");
        }
        c.setEliminado(Boolean.TRUE);
        repo.save(c);
    }

    public CitaResponseDTO obtener(Long id, Long usuarioId) {
        Cita c = repo.findOneByIdAndUsuario(id, usuarioId);
        if (c == null) {
            throw new NotFoundException("Cita no encontrada");
        }
        return CitaMapper.toDTO(c);
    }

    public Page<CitaResponseDTO> listarRango(Long usuarioId, String desde, String hasta, int page, int size) {
        LocalDate d1 = LocalDate.parse(desde);
        LocalDate d2 = LocalDate.parse(hasta);
        Pageable p = PageRequest.of(page, size);
        Page<Cita> res = repo.listarPorRango(usuarioId, d1, d2, p);
        List<CitaResponseDTO> list = new ArrayList<CitaResponseDTO>();
        int i;
        for (i = 0; i < res.getContent().size(); i++) {
            list.add(CitaMapper.toDTO(res.getContent().get(i)));
        }
        return new PageImpl<CitaResponseDTO>(list, p, res.getTotalElements());
    }

    public Page<CitaResponseDTO> listarPorEstado(Long usuarioId, Long estadoId, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        Page<Cita> res = repo.listarPorEstado(usuarioId, estadoId, p);
        List<CitaResponseDTO> list = new ArrayList<CitaResponseDTO>();
        int i;
        for (i = 0; i < res.getContent().size(); i++) {
            list.add(CitaMapper.toDTO(res.getContent().get(i)));
        }
        return new PageImpl<CitaResponseDTO>(list, p, res.getTotalElements());
    }

    public Page<CitaResponseDTO> listarPorTipo(Long usuarioId, Long tipoId, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        Page<Cita> res = repo.listarPorTipo(usuarioId, tipoId, p);
        List<CitaResponseDTO> list = new ArrayList<CitaResponseDTO>();
        int i;
        for (i = 0; i < res.getContent().size(); i++) {
            list.add(CitaMapper.toDTO(res.getContent().get(i)));
        }
        return new PageImpl<CitaResponseDTO>(list, p, res.getTotalElements());
    }

    public Page<CitaResponseDTO> listarPorMedico(Long usuarioId, String medico, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        Page<Cita> res = repo.listarPorMedico(usuarioId, medico, p);
        List<CitaResponseDTO> list = new ArrayList<CitaResponseDTO>();
        int i;
        for (i = 0; i < res.getContent().size(); i++) {
            list.add(CitaMapper.toDTO(res.getContent().get(i)));
        }
        return new PageImpl<CitaResponseDTO>(list, p, res.getTotalElements());
    }

    public Page<CitaResponseDTO> listarPorBebe(Long usuarioId, Long bebeId, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        Page<Cita> res = repo.listarPorBebe(usuarioId, bebeId, p);
        List<CitaResponseDTO> list = new ArrayList<CitaResponseDTO>();
        int i;
        for (i = 0; i < res.getContent().size(); i++) {
            list.add(CitaMapper.toDTO(res.getContent().get(i)));
        }
        return new PageImpl<CitaResponseDTO>(list, p, res.getTotalElements());
    }
}
