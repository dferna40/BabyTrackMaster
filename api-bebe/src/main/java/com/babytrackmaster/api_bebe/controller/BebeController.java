package com.babytrackmaster.api_bebe.controller;

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

import com.babytrackmaster.api_bebe.dto.BebeRequest;
import com.babytrackmaster.api_bebe.dto.BebeResponse;
import com.babytrackmaster.api_bebe.security.JwtService;
import com.babytrackmaster.api_bebe.service.BebeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bebes")
@RequiredArgsConstructor
@Tag(name = "Bebes", description = "Gestión de bebés")
public class BebeController {

    private final BebeService service;
    private final JwtService jwtService;

    @Operation(summary = "Crear bebé", description = "La imagen debe enviarse como Base64 en el campo imagenBebe")
    @PostMapping
    public ResponseEntity<BebeResponse> crear(@Valid @RequestBody BebeRequest req) {
        Long usuarioId = jwtService.resolveUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(usuarioId, req));
    }

    @Operation(summary = "Actualizar bebé", description = "La imagen debe enviarse como Base64 en el campo imagenBebe")
    @PutMapping("/{id}")
    public ResponseEntity<BebeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody BebeRequest req) {
        Long usuarioId = jwtService.resolveUserId();
        return ResponseEntity.ok(service.actualizar(usuarioId, id, req));
    }

    @Operation(summary = "Eliminar bebé")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Long usuarioId = jwtService.resolveUserId();
        service.eliminar(usuarioId, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener bebé")
    @GetMapping("/{id}")
    public ResponseEntity<BebeResponse> obtener(@PathVariable Long id) {
        Long usuarioId = jwtService.resolveUserId();
        return ResponseEntity.ok(service.obtener(usuarioId, id));
    }

    @Operation(summary = "Listar bebés")
    @GetMapping
    public List<BebeResponse> listar() {
        Long usuarioId = jwtService.resolveUserId();
        return service.listar(usuarioId);
    }
}
