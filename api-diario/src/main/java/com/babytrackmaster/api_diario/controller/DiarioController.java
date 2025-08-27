package com.babytrackmaster.api_diario.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_diario.dto.DiarioCreateDTO;
import com.babytrackmaster.api_diario.dto.DiarioResponseDTO;
import com.babytrackmaster.api_diario.dto.DiarioUpdateDTO;
import com.babytrackmaster.api_diario.dto.PageResponseDTO;
import com.babytrackmaster.api_diario.service.DiarioService;
import com.babytrackmaster.api_diario.security.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Diario", description = "Gestión de entradas del diario del bebé")
@RestController
@RequestMapping("/api/v1/diario")
@RequiredArgsConstructor
public class DiarioController {

	private final DiarioService service;
	private final JwtService jwtService;

	private Long getUsuarioIdFromHeader(String header) {
		return Long.valueOf(header);
	}

	@Operation(summary = "Crear entrada de diario")
	@ApiResponse(responseCode = "201", description = "Creado")
	@PostMapping
	public ResponseEntity<DiarioResponseDTO> crear(@Valid @RequestBody DiarioCreateDTO dto) {
		Long usuarioId = jwtService.resolveUserId();
		DiarioResponseDTO resp = service.crear(usuarioId, dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}

	@Operation(summary = "Obtener entrada por id")
	@GetMapping("/{id}")
	public ResponseEntity<DiarioResponseDTO> obtener(
			@PathVariable Long id) {
		Long usuarioId = jwtService.resolveUserId();
		return ResponseEntity.ok(service.obtener(usuarioId, id));
	}

	@Operation(summary = "Actualizar entrada por id")
	@PutMapping("/{id}")
	public ResponseEntity<DiarioResponseDTO> actualizar(
			@PathVariable Long id, @Valid @RequestBody DiarioUpdateDTO dto) {
		Long usuarioId = jwtService.resolveUserId();
		return ResponseEntity.ok(service.actualizar(usuarioId, id, dto));
	}

	@Operation(summary = "Eliminar (lógico) entrada por id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Long usuarioId = jwtService.resolveUserId();
		service.eliminar(usuarioId, id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Listar por rango de fechas (incluidos)")
	@GetMapping
	public ResponseEntity<PageResponseDTO<DiarioResponseDTO>> listarRango(
			@RequestParam String desde, @RequestParam String hasta,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Long usuarioId = jwtService.resolveUserId();
		return ResponseEntity.ok(service.listarRango(usuarioId, desde, hasta, page, size));
	}

	@Operation(summary = "Listar por día")
	@GetMapping("/dia/{fecha}")
	public ResponseEntity<PageResponseDTO<DiarioResponseDTO>> listarDia(
			@PathVariable String fecha,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Long usuarioId = jwtService.resolveUserId();
		return ResponseEntity.ok(service.listarDia(usuarioId, fecha, page, size));
	}

	@Operation(summary = "Listar por etiqueta (contiene)")
	@GetMapping("/tags")
	public ResponseEntity<PageResponseDTO<DiarioResponseDTO>> listarPorTag(
			@RequestParam(required = false) String tag,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Long usuarioId = jwtService.resolveUserId();
		return ResponseEntity.ok(service.listarPorTag(usuarioId, tag, page, size));
	}
}
