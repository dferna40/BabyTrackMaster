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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_cuidados.dto.CuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;
import com.babytrackmaster.api_cuidados.service.CuidadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cuidados")
@Tag(name = "Cuidados", description = "Gestión de cuidados del bebé")
public class CuidadoController {

        private final CuidadoService service;

        public CuidadoController(CuidadoService service) {
                this.service = service;
        }

	// ---------------------------------------------------------------------
	// Crear
	// ---------------------------------------------------------------------
        @Operation(summary = "Crear un cuidado", description = "Crea un cuidado para un usuario específico")
        @PostMapping("/usuario/{usuarioId}")
        public ResponseEntity<CuidadoResponse> crear(
                        @PathVariable Long usuarioId,
                        @Valid @org.springframework.web.bind.annotation.RequestBody CuidadoRequest req) {
                return new ResponseEntity<CuidadoResponse>(service.crear(usuarioId, req), HttpStatus.CREATED);
        }

	// ---------------------------------------------------------------------
	// Actualizar
	// ---------------------------------------------------------------------
        @Operation(summary = "Actualizar un cuidado", description = "Actualiza los datos de un cuidado propio")
        @PutMapping("/usuario/{usuarioId}/{id}")
        public ResponseEntity<CuidadoResponse> actualizar(
                        @PathVariable Long usuarioId,
                        @Parameter(description = "ID del cuidado", example = "101") @PathVariable Long id,
                        @Valid @org.springframework.web.bind.annotation.RequestBody CuidadoRequest req) {
                return ResponseEntity.ok(service.actualizar(usuarioId, id, req));
        }

	// ---------------------------------------------------------------------
	// Eliminar
	// ---------------------------------------------------------------------
        @Operation(summary = "Eliminar un cuidado", description = "Elimina un cuidado propio")
        @DeleteMapping("/usuario/{usuarioId}/{id}")
        public ResponseEntity<Void> eliminar(
                        @PathVariable Long usuarioId,
                        @Parameter(description = "ID del cuidado", example = "101") @PathVariable Long id) {
                service.eliminar(usuarioId, id);
                return ResponseEntity.noContent().build();
        }

	// ---------------------------------------------------------------------
	// Obtener por ID
	// ---------------------------------------------------------------------
        @Operation(summary = "Obtener un cuidado", description = "Devuelve un cuidado propio por su ID")
        @GetMapping("/usuario/{usuarioId}/{id}")
        public ResponseEntity<CuidadoResponse> obtener(
                        @PathVariable Long usuarioId,
                        @PathVariable Long id) {
                return ResponseEntity.ok(service.obtener(usuarioId, id));
        }

	// ---------------------------------------------------------------------
	// Listar por bebé
	// ---------------------------------------------------------------------
        @Operation(summary = "Listar cuidados de un bebé", description = "Lista todos los cuidados de un bebé del usuario")
        @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}")
        public ResponseEntity<List<CuidadoResponse>> listarPorBebe(
                        @PathVariable Long usuarioId,
                        @Parameter(description = "ID del bebé", example = "1") @PathVariable Long bebeId,
                        @RequestParam(value = "limit", required = false) Integer limit) {
                return ResponseEntity.ok(service.listarPorBebe(usuarioId, bebeId, limit));
        }

	@Operation(summary = "Listar cuidados por bebé y tipo")
        @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/tipo/{tipoId}")
        public ResponseEntity<List<CuidadoResponse>> listarPorBebeYTipo(
                        @PathVariable Long usuarioId,
                        @PathVariable Long bebeId,
                        @PathVariable Long tipoId) {
                return ResponseEntity.ok(service.listarPorBebeYTipo(usuarioId, bebeId, tipoId));
        }

	@Operation(summary = "Listar cuidados por rango de fechas")
        @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/rango")
        public ResponseEntity<List<CuidadoResponse>> listarPorRango(
                        @PathVariable Long usuarioId,
                        @PathVariable Long bebeId,
                        @RequestParam("desde") Long desdeMillis,
                        @RequestParam("hasta") Long hastaMillis) {
                return ResponseEntity.ok(
                                service.listarPorRango(usuarioId, bebeId, new Date(desdeMillis), new Date(hastaMillis)));
        }
}
