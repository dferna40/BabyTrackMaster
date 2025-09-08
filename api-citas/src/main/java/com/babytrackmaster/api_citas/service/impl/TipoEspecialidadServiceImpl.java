package com.babytrackmaster.api_citas.service.impl;

import com.babytrackmaster.api_citas.dto.TipoEspecialidadCreateDTO;
import com.babytrackmaster.api_citas.dto.TipoEspecialidadResponseDTO;
import com.babytrackmaster.api_citas.dto.TipoEspecialidadUpdateDTO;
import com.babytrackmaster.api_citas.entity.TipoEspecialidad;
import com.babytrackmaster.api_citas.exception.BadRequestException;
import com.babytrackmaster.api_citas.exception.NotFoundException;
import com.babytrackmaster.api_citas.mapper.TipoEspecialidadMapper;
import com.babytrackmaster.api_citas.repository.TipoEspecialidadRepository;
import com.babytrackmaster.api_citas.service.TipoEspecialidadService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TipoEspecialidadServiceImpl implements TipoEspecialidadService {

    private final TipoEspecialidadRepository repo;

    @Override
    public TipoEspecialidadResponseDTO crear(TipoEspecialidadCreateDTO dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new BadRequestException("Tipo de especialidad ya existe");
        }
        TipoEspecialidad entity = TipoEspecialidadMapper.toEntity(dto);
        entity = repo.save(entity);
        return TipoEspecialidadMapper.toDTO(entity);
    }

    @Override
    public TipoEspecialidadResponseDTO actualizar(Long id, TipoEspecialidadUpdateDTO dto) {
        TipoEspecialidad entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de especialidad no encontrado"));
        if (dto.getNombre() != null && repo.existsByNombreIgnoreCase(dto.getNombre())
                && !dto.getNombre().equalsIgnoreCase(entity.getNombre())) {
            throw new BadRequestException("Tipo de especialidad ya existe");
        }
        TipoEspecialidadMapper.applyUpdate(entity, dto);
        entity = repo.save(entity);
        return TipoEspecialidadMapper.toDTO(entity);
    }

    @Override
    public void eliminar(Long id) {
        TipoEspecialidad entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de especialidad no encontrado"));
        repo.delete(entity);
    }

    @Override
    public TipoEspecialidadResponseDTO obtener(Long id) {
        TipoEspecialidad entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de especialidad no encontrado"));
        return TipoEspecialidadMapper.toDTO(entity);
    }

    @Override
    public List<TipoEspecialidadResponseDTO> listarTodos() {
        List<TipoEspecialidad> list = repo.findAll();
        List<TipoEspecialidadResponseDTO> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            res.add(TipoEspecialidadMapper.toDTO(list.get(i)));
        }
        return res;
    }
}
