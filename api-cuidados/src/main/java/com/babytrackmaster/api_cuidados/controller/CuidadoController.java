package com.babytrackmaster.api_cuidados.controller;

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

import com.babytrackmaster.api_cuidados.dto.CuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;
import com.babytrackmaster.api_cuidados.service.CuidadoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cuidados")
public class CuidadoController {

    private final CuidadoService service;

    public CuidadoController(CuidadoService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un cuidado")
    @PostMapping
    public ResponseEntity<CuidadoResponse> crear(@Valid @RequestBody CuidadoRequest req) {
        return new ResponseEntity<CuidadoResponse>(service.crear(req), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un cuidado")
    @PutMapping("/{id}")
    public ResponseEntity<CuidadoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody CuidadoRequest req) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }

    @Operation(summary = "Eliminar un cuidado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener un cuidado por id")
    @GetMapping("/{id}")
    public ResponseEntity<CuidadoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @Operation(summary = "Listar cuidados por bebé")
    @GetMapping("/bebe/{bebeId}")
    public ResponseEntity<List<CuidadoResponse>> listarPorBebe(@PathVariable Long bebeId) {
        return ResponseEntity.ok(service.listarPorBebe(bebeId));
    }

    @Operation(summary = "Listar cuidados por bebé y tipo")
    @GetMapping("/bebe/{bebeId}/tipo/{tipo}")
    public ResponseEntity<List<CuidadoResponse>> listarPorBebeYTipo(@PathVariable Long bebeId, @PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorBebeYTipo(bebeId, tipo));
    }

    @Operation(summary = "Listar cuidados por rango de fechas")
    @GetMapping("/bebe/{bebeId}/rango")
    public ResponseEntity<List<CuidadoResponse>> listarPorRango(
            @PathVariable Long bebeId,
            @RequestParam("desde") Long desdeMillis,
            @RequestParam("hasta") Long hastaMillis) {
        return ResponseEntity.ok(service.listarPorRango(bebeId, new Date(desdeMillis), new Date(hastaMillis)));
    }
}
