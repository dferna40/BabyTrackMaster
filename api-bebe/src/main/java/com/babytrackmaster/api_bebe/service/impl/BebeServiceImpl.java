package com.babytrackmaster.api_bebe.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_bebe.dto.BebeRequest;
import com.babytrackmaster.api_bebe.dto.BebeResponse;
import com.babytrackmaster.api_bebe.entity.Bebe;
import com.babytrackmaster.api_bebe.exception.NotFoundException;
import com.babytrackmaster.api_bebe.mapper.BebeMapper;
import com.babytrackmaster.api_bebe.repository.BebeRepository;
import com.babytrackmaster.api_bebe.service.BebeService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BebeServiceImpl implements BebeService {

    private final BebeRepository repository;

    @Override
    public BebeResponse crear(Long usuarioId, BebeRequest request) {
        Bebe entity = BebeMapper.toEntity(request);
        entity.setUsuarioId(usuarioId);
        Bebe saved = repository.save(entity);
        return BebeMapper.toResponse(saved);
    }

    @Override
    public BebeResponse actualizar(Long usuarioId, Long id, BebeRequest request) {
        Bebe bebe = repository.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId)
            .orElseThrow(() -> new NotFoundException("Bebé no encontrado"));
        if (request.getNombre() != null) {
            bebe.setNombre(request.getNombre());
        }
        if (request.getFechaNacimiento() != null) {
            bebe.setFechaNacimiento(request.getFechaNacimiento());
        }
        if (request.getSexo() != null) {
            bebe.setSexo(request.getSexo());
        }
        Bebe saved = repository.save(bebe);
        return BebeMapper.toResponse(saved);
    }

    @Override
    public void eliminar(Long usuarioId, Long id) {
        Bebe bebe = repository.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId)
            .orElseThrow(() -> new NotFoundException("Bebé no encontrado"));
        bebe.setEliminado(true);
        repository.save(bebe);
    }

    @Override
    @Transactional(readOnly = true)
    public BebeResponse obtener(Long usuarioId, Long id) {
        Bebe bebe = repository.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId)
            .orElseThrow(() -> new NotFoundException("Bebé no encontrado"));
        return BebeMapper.toResponse(bebe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BebeResponse> listar(Long usuarioId) {
        return repository.findByUsuarioIdAndEliminadoFalseOrderByFechaNacimientoAsc(usuarioId)
            .stream().map(BebeMapper::toResponse).collect(Collectors.toList());
    }
}
