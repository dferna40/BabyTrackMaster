package com.babytrackmaster.api_cuidados.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_cuidados.dto.TipoPanalRequest;
import com.babytrackmaster.api_cuidados.dto.TipoPanalResponse;
import com.babytrackmaster.api_cuidados.service.TipoPanalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tipos-panal")
@Tag(name = "Tipos de pañal", description = "Gestión de tipos de pañal")
public class TipoPanalController {

    private final TipoPanalService service;

    public TipoPanalController(TipoPanalService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un tipo de pañal")
    @PostMapping
    public ResponseEntity<TipoPanalResponse> crear(
            @Valid @RequestBody TipoPanalRequest req) {
        return new ResponseEntity<TipoPanalResponse>(service.crear(req), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un tipo de pañal")
    @PutMapping("/{id}")
    public ResponseEntity<TipoPanalResponse> actualizar(
            @Parameter(description = "ID del tipo de pañal", example = "1") @PathVariable Long id,
            @Valid @RequestBody TipoPanalRequest req) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }

    @Operation(summary = "Eliminar un tipo de pañal")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del tipo de pañal", example = "1") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener un tipo de pañal")
    @GetMapping("/{id}")
    public ResponseEntity<TipoPanalResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @Operation(summary = "Listar tipos de pañal")
    @GetMapping
    public ResponseEntity<List<TipoPanalResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}

