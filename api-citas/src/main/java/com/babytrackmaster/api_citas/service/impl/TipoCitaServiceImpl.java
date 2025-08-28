package com.babytrackmaster.api_citas.service.impl;

import com.babytrackmaster.api_citas.dto.TipoCitaCreateDTO;
import com.babytrackmaster.api_citas.dto.TipoCitaResponseDTO;
import com.babytrackmaster.api_citas.dto.TipoCitaUpdateDTO;
import com.babytrackmaster.api_citas.entity.TipoCita;
import com.babytrackmaster.api_citas.exception.BadRequestException;
import com.babytrackmaster.api_citas.exception.NotFoundException;
import com.babytrackmaster.api_citas.mapper.TipoCitaMapper;
import com.babytrackmaster.api_citas.repository.TipoCitaRepository;
import com.babytrackmaster.api_citas.service.TipoCitaService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TipoCitaServiceImpl implements TipoCitaService {

    private final TipoCitaRepository repo;

    @Override
    public TipoCitaResponseDTO crear(TipoCitaCreateDTO dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new BadRequestException("Tipo de cita ya existe");
        }
        TipoCita tipo = TipoCitaMapper.toEntity(dto);
        tipo = repo.save(tipo);
        return TipoCitaMapper.toDTO(tipo);
    }

    @Override
    public TipoCitaResponseDTO actualizar(Long id, TipoCitaUpdateDTO dto) {
        TipoCita tipo = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de cita no encontrado"));
        if (dto.getNombre() != null && repo.existsByNombreIgnoreCase(dto.getNombre())
                && !dto.getNombre().equalsIgnoreCase(tipo.getNombre())) {
            throw new BadRequestException("Tipo de cita ya existe");
        }
        TipoCitaMapper.applyUpdate(tipo, dto);
        tipo = repo.save(tipo);
        return TipoCitaMapper.toDTO(tipo);
    }

    @Override
    public void eliminar(Long id) {
        TipoCita tipo = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de cita no encontrado"));
        repo.delete(tipo);
    }

    @Override
    public TipoCitaResponseDTO obtener(Long id) {
        TipoCita tipo = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de cita no encontrado"));
        return TipoCitaMapper.toDTO(tipo);
    }

    @Override
    public List<TipoCitaResponseDTO> listarTodos() {
        List<TipoCita> list = repo.findAll();
        List<TipoCitaResponseDTO> res = new ArrayList<>();
        int i;
        for (i = 0; i < list.size(); i++) {
            res.add(TipoCitaMapper.toDTO(list.get(i)));
        }
        return res;
    }
}
