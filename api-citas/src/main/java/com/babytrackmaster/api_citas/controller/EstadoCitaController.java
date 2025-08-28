package com.babytrackmaster.api_citas.controller;

import com.babytrackmaster.api_citas.dto.EstadoCitaCreateDTO;
import com.babytrackmaster.api_citas.dto.EstadoCitaResponseDTO;
import com.babytrackmaster.api_citas.dto.EstadoCitaUpdateDTO;
import com.babytrackmaster.api_citas.service.EstadoCitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Estados de Cita", description = "Gestión de estados de cita")
@RestController
@RequestMapping("/api/v1/estados-cita")
@RequiredArgsConstructor
public class EstadoCitaController {

    private final EstadoCitaService service;

    @Operation(summary = "Crear estado de cita")
    @PostMapping
    public ResponseEntity<EstadoCitaResponseDTO> crear(@Valid @RequestBody EstadoCitaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Actualizar estado de cita")
    @PutMapping("/{id}")
    public ResponseEntity<EstadoCitaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EstadoCitaUpdateDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar estado de cita")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener estado de cita por id")
    @GetMapping("/{id}")
    public ResponseEntity<EstadoCitaResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @Operation(summary = "Listar todos los estados de cita")
    @GetMapping
    public ResponseEntity<List<EstadoCitaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}

