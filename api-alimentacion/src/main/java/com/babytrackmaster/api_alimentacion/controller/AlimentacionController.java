package com.babytrackmaster.api_alimentacion.controller;

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

import com.babytrackmaster.api_alimentacion.dto.AlimentacionRequest;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionResponse;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionStatsResponse;
import com.babytrackmaster.api_alimentacion.entity.TipoLactancia;
import com.babytrackmaster.api_alimentacion.service.AlimentacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/alimentacion")
@Tag(name = "Alimentacion", description = "Gestión de alimentación del bebé")
public class AlimentacionController {

    private final AlimentacionService service;

    public AlimentacionController(AlimentacionService service) {
        this.service = service;
    }

    @Operation(summary = "Crear registro de alimentación")
    @PostMapping("/usuario/{usuarioId}/bebe/{bebeId}")
    public ResponseEntity<AlimentacionResponse> crear(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @Valid @RequestBody AlimentacionRequest req) {
        return new ResponseEntity<>(service.crear(usuarioId, bebeId, req), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar registro de alimentación")
    @PutMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}")
    public ResponseEntity<AlimentacionResponse> actualizar(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @PathVariable Long id,
            @Valid @RequestBody AlimentacionRequest req) {
        return ResponseEntity.ok(service.actualizar(usuarioId, bebeId, id, req));
    }

    @Operation(summary = "Eliminar registro de alimentación")
    @DeleteMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @PathVariable Long id) {
        service.eliminar(usuarioId, bebeId, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener registro de alimentación")
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}")
    public ResponseEntity<AlimentacionResponse> obtener(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(usuarioId, bebeId, id));
    }

    @Operation(summary = "Listar alimentación de un bebé")
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}")
    public ResponseEntity<List<AlimentacionResponse>> listar(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId) {
        return ResponseEntity.ok(service.listar(usuarioId, bebeId));
    }

    @Operation(summary = "Obtener estadísticas de alimentación")
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/stats")
    public ResponseEntity<AlimentacionStatsResponse> stats(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId) {
        return ResponseEntity.ok(service.stats(usuarioId, bebeId));
    }

    @Operation(summary = "Listar tipos de lactancia")
    @GetMapping("/tipos-lactancia")
    public ResponseEntity<List<TipoLactancia>> tiposLactancia() {
        return ResponseEntity.ok(service.listarTiposLactancia());
    }
}
