package com.babytrackmaster.api_crecimiento.controller;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_crecimiento.dto.CrecimientoRequest;
import com.babytrackmaster.api_crecimiento.dto.CrecimientoResponse;
import com.babytrackmaster.api_crecimiento.dto.CrecimientoStatsResponse;
import com.babytrackmaster.api_crecimiento.service.CrecimientoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/crecimientos")
@Tag(name = "Crecimientos", description = "Gestión de registros de crecimiento")
public class CrecimientoController {

    private final CrecimientoService service;

    public CrecimientoController(CrecimientoService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un registro de crecimiento")
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<CrecimientoResponse> crear(
            @PathVariable Long usuarioId,
            @Valid @RequestBody CrecimientoRequest req) {
        return new ResponseEntity<>(service.crear(usuarioId, req), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un registro de crecimiento")
    @PutMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<CrecimientoResponse> actualizar(
            @PathVariable Long usuarioId,
            @PathVariable Long id,
            @Valid @RequestBody CrecimientoRequest req) {
        return ResponseEntity.ok(service.actualizar(usuarioId, id, req));
    }

    @Operation(summary = "Eliminar un registro de crecimiento")
    @DeleteMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long usuarioId,
            @PathVariable Long id) {
        service.eliminar(usuarioId, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener un registro de crecimiento")
    @GetMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<CrecimientoResponse> obtener(
            @PathVariable Long usuarioId,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(usuarioId, id));
    }

    @Operation(summary = "Listar registros de un bebé")
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}")
    public ResponseEntity<List<CrecimientoResponse>> listarPorBebe(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @RequestParam(value = "limit", required = false) Integer limit) {
        return ResponseEntity.ok(service.listarPorBebe(usuarioId, bebeId, limit));
    }

    @Operation(summary = "Listar registros por bebé y tipo")
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/tipo/{tipoId}")
    public ResponseEntity<List<CrecimientoResponse>> listarPorBebeYTipo(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @PathVariable Long tipoId,
            @RequestParam(value = "limit", required = false) Integer limit) {
        return ResponseEntity.ok(service.listarPorBebeYTipo(usuarioId, bebeId, tipoId, limit));
    }

    @Operation(summary = "Listar las últimas mediciones por tipo")
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/tipo/{tipoId}/ultimos")
    public ResponseEntity<List<CrecimientoResponse>> listarUltimosPorTipo(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @PathVariable Long tipoId,
            @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(service.listarUltimosPorTipo(usuarioId, bebeId, tipoId, limit));
    }

    @Operation(summary = "Listar registros por rango de fechas")
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/rango")
    public ResponseEntity<List<CrecimientoResponse>> listarPorRango(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @RequestParam("desde") Long desdeMillis,
            @RequestParam("hasta") Long hastaMillis) {
        return ResponseEntity.ok(
                service.listarPorRango(usuarioId, bebeId, new Date(desdeMillis), new Date(hastaMillis)));
    }

    @Operation(summary = "Obtener estadísticas básicas")
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/stats")
    public ResponseEntity<CrecimientoStatsResponse> obtenerStats(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @RequestParam("tipoId") Long tipoId,
            @RequestParam("desde") Long desdeMillis,
            @RequestParam("hasta") Long hastaMillis) {
        return ResponseEntity.ok(
                service.obtenerEstadisticas(usuarioId, bebeId, tipoId, new Date(desdeMillis), new Date(hastaMillis)));
    }
}
