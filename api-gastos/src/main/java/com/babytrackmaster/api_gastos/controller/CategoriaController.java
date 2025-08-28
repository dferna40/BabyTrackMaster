package com.babytrackmaster.api_gastos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
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

    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría de gasto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
