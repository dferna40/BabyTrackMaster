package com.babytrackmaster.api_citas.controller;

import com.babytrackmaster.api_citas.dto.TipoCitaCreateDTO;
import com.babytrackmaster.api_citas.dto.TipoCitaResponseDTO;
import com.babytrackmaster.api_citas.dto.TipoCitaUpdateDTO;
import com.babytrackmaster.api_citas.service.TipoCitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tipos de Cita", description = "Gestión de tipos de cita")
@RestController
@RequestMapping("/api/v1/tipos-cita")
@RequiredArgsConstructor
public class TipoCitaController {

    private final TipoCitaService service;

    @Operation(summary = "Crear tipo de cita")
    @PostMapping
    public ResponseEntity<TipoCitaResponseDTO> crear(@Valid @RequestBody TipoCitaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Actualizar tipo de cita")
    @PutMapping("/{id}")
    public ResponseEntity<TipoCitaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TipoCitaUpdateDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar tipo de cita")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener tipo de cita por id")
    @GetMapping("/{id}")
    public ResponseEntity<TipoCitaResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @Operation(summary = "Listar todos los tipos de cita")
    @GetMapping
    public ResponseEntity<List<TipoCitaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}
