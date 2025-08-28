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

import com.babytrackmaster.api_cuidados.dto.TipoCuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.TipoCuidadoResponse;
import com.babytrackmaster.api_cuidados.service.TipoCuidadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tipos-cuidado")
@Tag(name = "Tipos de cuidado", description = "Gestión de tipos de cuidado")
public class TipoCuidadoController {

    private final TipoCuidadoService service;

    public TipoCuidadoController(TipoCuidadoService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un tipo de cuidado")
    @PostMapping
    public ResponseEntity<TipoCuidadoResponse> crear(
            @Valid @RequestBody TipoCuidadoRequest req) {
        return new ResponseEntity<TipoCuidadoResponse>(service.crear(req), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un tipo de cuidado")
    @PutMapping("/{id}")
    public ResponseEntity<TipoCuidadoResponse> actualizar(
            @Parameter(description = "ID del tipo de cuidado", example = "1") @PathVariable Long id,
            @Valid @RequestBody TipoCuidadoRequest req) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }

    @Operation(summary = "Eliminar un tipo de cuidado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del tipo de cuidado", example = "1") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener un tipo de cuidado")
    @GetMapping("/{id}")
    public ResponseEntity<TipoCuidadoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @Operation(summary = "Listar tipos de cuidado")
    @GetMapping
    public ResponseEntity<List<TipoCuidadoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}
