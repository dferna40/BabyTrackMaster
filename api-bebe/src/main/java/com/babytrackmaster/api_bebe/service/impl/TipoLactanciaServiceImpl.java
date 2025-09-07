package com.babytrackmaster.api_bebe.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_bebe.dto.TipoLactanciaResponse;
import com.babytrackmaster.api_bebe.mapper.TipoLactanciaMapper;
import com.babytrackmaster.api_bebe.repository.TipoLactanciaRepository;
import com.babytrackmaster.api_bebe.service.TipoLactanciaService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TipoLactanciaServiceImpl implements TipoLactanciaService {

    private final TipoLactanciaRepository repository;

    @Override
    public List<TipoLactanciaResponse> listar() {
        return repository.findAll().stream()
                .map(TipoLactanciaMapper::toResponse)
                .collect(Collectors.toList());
    }
}
