package com.babytrackmaster.api_crecimiento.controller;

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

import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoRequest;
import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoResponse;
import com.babytrackmaster.api_crecimiento.service.TipoCrecimientoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tipos-crecimiento")
@Tag(name = "Tipos de crecimiento", description = "Gestión de tipos de crecimiento")
public class TipoCrecimientoController {

    private final TipoCrecimientoService service;

    public TipoCrecimientoController(TipoCrecimientoService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un tipo de crecimiento")
    @PostMapping
    public ResponseEntity<TipoCrecimientoResponse> crear(
            @Valid @RequestBody TipoCrecimientoRequest req) {
        return new ResponseEntity<>(service.crear(req), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un tipo de crecimiento")
    @PutMapping("/{id}")
    public ResponseEntity<TipoCrecimientoResponse> actualizar(
            @Parameter(description = "ID del tipo de crecimiento", example = "1") @PathVariable Long id,
            @Valid @RequestBody TipoCrecimientoRequest req) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }

    @Operation(summary = "Eliminar un tipo de crecimiento")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del tipo de crecimiento", example = "1") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener un tipo de crecimiento")
    @GetMapping("/{id}")
    public ResponseEntity<TipoCrecimientoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @Operation(summary = "Listar tipos de crecimiento")
    @GetMapping
    public ResponseEntity<List<TipoCrecimientoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}
