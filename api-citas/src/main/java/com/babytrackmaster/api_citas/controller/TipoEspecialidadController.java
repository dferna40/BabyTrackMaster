package com.babytrackmaster.api_citas.controller;

import com.babytrackmaster.api_citas.dto.TipoEspecialidadCreateDTO;
import com.babytrackmaster.api_citas.dto.TipoEspecialidadResponseDTO;
import com.babytrackmaster.api_citas.dto.TipoEspecialidadUpdateDTO;
import com.babytrackmaster.api_citas.service.TipoEspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tipos de Especialidad", description = "Gestión de tipos de especialidad")
@RestController
@RequestMapping("/api/v1/tipos-especialidad")
@RequiredArgsConstructor
public class TipoEspecialidadController {

    private final TipoEspecialidadService service;

    @Operation(summary = "Crear tipo de especialidad")
    @PostMapping
    public ResponseEntity<TipoEspecialidadResponseDTO> crear(@Valid @RequestBody TipoEspecialidadCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Actualizar tipo de especialidad")
    @PutMapping("/{id}")
    public ResponseEntity<TipoEspecialidadResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody TipoEspecialidadUpdateDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar tipo de especialidad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener tipo de especialidad por id")
    @GetMapping("/{id}")
    public ResponseEntity<TipoEspecialidadResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @Operation(summary = "Listar todos los tipos de especialidad")
    @GetMapping
    public ResponseEntity<List<TipoEspecialidadResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}
