package com.babytrackmaster.api_gastos.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
import com.babytrackmaster.api_gastos.dto.CategoriaUpdateRequest;
import com.babytrackmaster.api_gastos.entity.CategoriaGasto;
import com.babytrackmaster.api_gastos.mapper.CategoriaMapper;
import com.babytrackmaster.api_gastos.repository.CategoriaGastoRepository;
import com.babytrackmaster.api_gastos.service.CategoriaService;
import com.babytrackmaster.api_gastos.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaGastoRepository categoriaRepository;

    @Override
    public CategoriaResponse crear(CategoriaCreateRequest req) {
        CategoriaGasto c = new CategoriaGasto();
        c.setNombre(req.getNombre());
        c = categoriaRepository.save(c);
        return CategoriaMapper.toResponse(c);
    }

    @Override
    public CategoriaResponse actualizar(Long id, CategoriaUpdateRequest req) {
        CategoriaGasto existente = categoriaRepository.findOneById(id);
        if (existente == null) {
            throw new NotFoundException("Categoría no encontrada");
        }
        existente.setNombre(req.getNombre());
        existente = categoriaRepository.save(existente);
        return CategoriaMapper.toResponse(existente);
    }
}
