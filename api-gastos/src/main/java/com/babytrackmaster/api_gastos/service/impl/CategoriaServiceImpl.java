package com.babytrackmaster.api_gastos.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
import com.babytrackmaster.api_gastos.entity.CategoriaGasto;
import com.babytrackmaster.api_gastos.mapper.CategoriaMapper;
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
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        List<CategoriaGasto> categorias = categoriaRepository.findAll(Sort.by("nombre"));
        List<CategoriaResponse> dtos = new ArrayList<CategoriaResponse>();
        int i = 0;
        while (i < categorias.size()) {
            dtos.add(CategoriaMapper.toResponse(categorias.get(i)));
            i++;
        }
        return dtos;
    }
}
