package com.babytrackmaster.api_gastos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
import com.babytrackmaster.api_gastos.dto.CategoriaUpdateRequest;
import com.babytrackmaster.api_gastos.security.JwtService;
import com.babytrackmaster.api_gastos.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Gestión de categorías de gasto")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final JwtService jwtService;

    @Operation(summary = "Crear una categoría", description = "Crea una nueva categoría de gasto")
    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaCreateRequest req) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CategoriaResponse resp = categoriaService.crear(req);
        return new ResponseEntity<CategoriaResponse>(resp, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una categoría", description = "Actualiza una categoría de gasto existente")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(@PathVariable Long id,
            @Valid @RequestBody CategoriaUpdateRequest req) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CategoriaResponse resp = categoriaService.actualizar(id, req);
        return ResponseEntity.ok(resp);
    }
}
