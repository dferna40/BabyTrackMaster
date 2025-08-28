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
	@Operation(summary = "Crear un cuidado", description = "Crea un cuidado para el usuario autenticado")
	@PostMapping
	public ResponseEntity<CuidadoResponse> crear(
			@Valid @org.springframework.web.bind.annotation.RequestBody CuidadoRequest req) {
		return new ResponseEntity<CuidadoResponse>(service.crear(req), HttpStatus.CREATED);
	}

	// ---------------------------------------------------------------------
	// Actualizar
	// ---------------------------------------------------------------------
	@Operation(summary = "Actualizar un cuidado", description = "Actualiza los datos de un cuidado propio")
	@PutMapping("/{id}")
	public ResponseEntity<CuidadoResponse> actualizar(
			@Parameter(description = "ID del cuidado", example = "101") @PathVariable Long id,
			@Valid @org.springframework.web.bind.annotation.RequestBody CuidadoRequest req) {
		return ResponseEntity.ok(service.actualizar(id, req));
	}

	// ---------------------------------------------------------------------
	// Eliminar
	// ---------------------------------------------------------------------
	@Operation(summary = "Eliminar un cuidado", description = "Elimina un cuidado propio")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(
			@Parameter(description = "ID del cuidado", example = "101") @PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}

	// ---------------------------------------------------------------------
	// Obtener por ID
	// ---------------------------------------------------------------------
	@Operation(summary = "Obtener un cuidado", description = "Devuelve un cuidado propio por su ID")
	@GetMapping("/{id}")
	public ResponseEntity<CuidadoResponse> obtener(@PathVariable Long id) {
		return ResponseEntity.ok(service.obtener(id));
	}

	// ---------------------------------------------------------------------
	// Listar por bebé
	// ---------------------------------------------------------------------
	@Operation(summary = "Listar cuidados de un bebé", description = "Lista todos los cuidados de un bebé del usuario autenticado")
	@GetMapping("/bebe/{bebeId}")
	public ResponseEntity<List<CuidadoResponse>> listarPorBebe(
			@Parameter(description = "ID del bebé", example = "1") @PathVariable Long bebeId) {
		return ResponseEntity.ok(service.listarPorBebe(bebeId));
	}

	@Operation(summary = "Listar cuidados por bebé y tipo")
        @GetMapping("/bebe/{bebeId}/tipo/{tipoId}")
        public ResponseEntity<List<CuidadoResponse>> listarPorBebeYTipo(@PathVariable Long bebeId,
                        @PathVariable Long tipoId) {
                return ResponseEntity.ok(service.listarPorBebeYTipo(bebeId, tipoId));
        }

	@Operation(summary = "Listar cuidados por rango de fechas")
	@GetMapping("/bebe/{bebeId}/rango")
	public ResponseEntity<List<CuidadoResponse>> listarPorRango(@PathVariable Long bebeId,
			@RequestParam("desde") Long desdeMillis, @RequestParam("hasta") Long hastaMillis) {
		return ResponseEntity.ok(service.listarPorRango(bebeId, new Date(desdeMillis), new Date(hastaMillis)));
	}
}
