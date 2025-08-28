package com.babytrackmaster.api_gastos.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
import com.babytrackmaster.api_gastos.entity.CategoriaGasto;
import com.babytrackmaster.api_gastos.mapper.CategoriaMapper;
import com.babytrackmaster.api_gastos.exception.NotFoundException;
import com.babytrackmaster.api_gastos.repository.CategoriaGastoRepository;
import com.babytrackmaster.api_gastos.service.CategoriaService;

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
    public CategoriaResponse obtener(Long id) {
        CategoriaGasto c = categoriaRepository.findOneById(id);
        if (c == null) {
            throw new NotFoundException("Categoría no encontrada");
        }
        return CategoriaMapper.toResponse(c);
    }
}
