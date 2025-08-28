package com.babytrackmaster.api_gastos.service;

import java.util.List;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;

public interface CategoriaService {
    CategoriaResponse crear(CategoriaCreateRequest req);
    List<CategoriaResponse> listar();
}
