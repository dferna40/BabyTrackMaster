package com.babytrackmaster.api_citas.service.impl;

import com.babytrackmaster.api_citas.dto.EstadoCitaCreateDTO;
import com.babytrackmaster.api_citas.dto.EstadoCitaResponseDTO;
import com.babytrackmaster.api_citas.dto.EstadoCitaUpdateDTO;
import com.babytrackmaster.api_citas.entity.EstadoCita;
import com.babytrackmaster.api_citas.exception.BadRequestException;
import com.babytrackmaster.api_citas.exception.NotFoundException;
import com.babytrackmaster.api_citas.mapper.EstadoCitaMapper;
import com.babytrackmaster.api_citas.repository.EstadoCitaRepository;
import com.babytrackmaster.api_citas.service.EstadoCitaService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstadoCitaServiceImpl implements EstadoCitaService {

    private final EstadoCitaRepository repo;

    @Override
    public EstadoCitaResponseDTO crear(EstadoCitaCreateDTO dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new BadRequestException("Estado de cita ya existe");
        }
        EstadoCita estado = EstadoCitaMapper.toEntity(dto);
        estado = repo.save(estado);
        return EstadoCitaMapper.toDTO(estado);
    }

    @Override
    public EstadoCitaResponseDTO actualizar(Long id, EstadoCitaUpdateDTO dto) {
        EstadoCita estado = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Estado de cita no encontrado"));
        if (dto.getNombre() != null && repo.existsByNombreIgnoreCase(dto.getNombre())
                && !dto.getNombre().equalsIgnoreCase(estado.getNombre())) {
            throw new BadRequestException("Estado de cita ya existe");
        }
        EstadoCitaMapper.applyUpdate(estado, dto);
        estado = repo.save(estado);
        return EstadoCitaMapper.toDTO(estado);
    }

    @Override
    public void eliminar(Long id) {
        EstadoCita estado = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Estado de cita no encontrado"));
        repo.delete(estado);
    }

    @Override
    public EstadoCitaResponseDTO obtener(Long id) {
        EstadoCita estado = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Estado de cita no encontrado"));
        return EstadoCitaMapper.toDTO(estado);
    }

    @Override
    public List<EstadoCitaResponseDTO> listarTodos() {
        List<EstadoCita> list = repo.findAll();
        List<EstadoCitaResponseDTO> res = new ArrayList<>();
        for (EstadoCita estado : list) {
            res.add(EstadoCitaMapper.toDTO(estado));
        }
        return res;
    }
}

