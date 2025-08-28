package com.babytrackmaster.api_gastos.service;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
import com.babytrackmaster.api_gastos.dto.CategoriaUpdateRequest;

public interface CategoriaService {
    CategoriaResponse crear(CategoriaCreateRequest req);

    CategoriaResponse actualizar(Long id, CategoriaUpdateRequest req);
}
