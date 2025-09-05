package com.babytrackmaster.api_bebe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_bebe.dto.TipoLactanciaResponse;
import com.babytrackmaster.api_bebe.service.TipoLactanciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tipo-lactancias")
@RequiredArgsConstructor
@Tag(name = "Tipos de lactancia", description = "Obtención de tipos de lactancia")
public class TipoLactanciaController {

    private final TipoLactanciaService service;

    @GetMapping
    @Operation(summary = "Listar tipos de lactancia")
    public List<TipoLactanciaResponse> listar() {
        return service.listar();
    }
}
