package com.babytrackmaster.api_bebe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_bebe.entity.TipoAlergia;
import com.babytrackmaster.api_bebe.repository.TipoAlergiaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tipo-alergias")
@RequiredArgsConstructor
@Tag(name = "Tipos de alergia", description = "Obtención de tipos de alergia")
public class TipoAlergiaController {

    private final TipoAlergiaRepository repository;

    @GetMapping
    @Operation(summary = "Listar tipos de alergia")
    public List<TipoAlergia> listar() {
        return repository.findAll();
    }
}
