package com.babytrackmaster.api_bebe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_bebe.entity.TipoGrupoSanguineo;
import com.babytrackmaster.api_bebe.repository.TipoGrupoSanguineoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tipo-gruposanguineo")
@RequiredArgsConstructor
@Tag(name = "Tipos de grupo sanguíneo", description = "Obtención de tipos de grupo sanguíneo")
public class TipoGrupoSanguineoController {

    private final TipoGrupoSanguineoRepository repository;

    @GetMapping
    @Operation(summary = "Listar tipos de grupo sanguíneo")
    public List<TipoGrupoSanguineo> listar() {
        return repository.findAll();
    }
}
