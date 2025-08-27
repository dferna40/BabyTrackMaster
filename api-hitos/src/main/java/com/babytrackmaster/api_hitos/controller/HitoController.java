package com.babytrackmaster.api_hitos.controller;

import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_hitos.dto.HitoRequest;
import com.babytrackmaster.api_hitos.dto.HitoResponse;
import com.babytrackmaster.api_hitos.security.JwtService;
import com.babytrackmaster.api_hitos.service.HitoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hitos")
@RequiredArgsConstructor
@Tag(name = "Hitos", description = "Gestión de hitos del bebé")
public class HitoController {

	private final HitoService service;
	private final JwtService jwtService;

	// Crear
	@Operation(summary = "Crear hito", description = "Crea un nuevo hito para el usuario autenticado")
	@PostMapping
	public ResponseEntity<HitoResponse> crear(@Valid @RequestBody HitoRequest req) {
		Long userId = jwtService.resolveUserId();
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		HitoResponse resp = service.crear(userId, req);
		return new ResponseEntity<HitoResponse>(resp, HttpStatus.CREATED);
	}

	// Actualizar
	@Operation(summary = "Actualizar hito", description = "Actualiza un hito propio")
	@PutMapping("/{id}")
	public ResponseEntity<Object> actualizar(Authentication auth, @PathVariable Long id,
			@Valid @RequestBody HitoRequest request) {
		Long userId = jwtService.resolveUserId();
		HitoResponse resp = service.actualizar(userId, id, request);
		return ResponseEntity.ok(resp);
	}

	// Eliminar
	@Operation(summary = "Eliminar hito", description = "Elimina un hito propio")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Long userId = jwtService.resolveUserId();
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		service.eliminar(userId, id);
		return ResponseEntity.noContent().build();
	}

	// Obtener por id
	@Operation(summary = "Obtener hito", description = "Obtiene un hito propio por su ID")
	@GetMapping("/{id}")
	public ResponseEntity<HitoResponse> obtener(@PathVariable Long id) {
		Long userId = jwtService.resolveUserId();
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(service.obtener(userId, id));
	}

	// Listar todos los hitos del usuario
	@Operation(summary = "Listar hitos del usuario", description = "Lista todos los hitos del usuario autenticado")
	@GetMapping
	public List<HitoResponse> listar(Authentication auth) {
		return service.listar(jwtService.resolveUserId());
	}

	// Listar por bebé
	@Operation(summary = "Listar hitos por bebé", description = "Lista todos los hitos de un bebé del usuario")
	@GetMapping("/bebe/{bebeId}")
	public List<HitoResponse> listarPorBebe(Authentication auth, @PathVariable Long bebeId) {
		return service.listarPorBebe(jwtService.resolveUserId(), bebeId);
	}

	// Listar por mes (YYYY-MM)
	@Operation(summary = "Listar hitos por mes (YYYY-MM)", description = "Lista todos los hitos del usuario en un mes")
	@GetMapping("/mes/{yyyyMM}")
	public List<HitoResponse> listarPorMes(Authentication auth,
			@PathVariable("yyyyMM") @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
		return service.listarPorMes(jwtService.resolveUserId(), mes);
	}

	// Buscar por título (contiene)
	@Operation(summary = "Buscar hitos por título", description = "Busca hitos del usuario cuyo título contenga el texto")
	@GetMapping("/buscar")
	public List<HitoResponse> buscarPorTitulo(Authentication auth, @RequestParam("q") String q) {
		return service.buscarPorTitulo(jwtService.resolveUserId(), q);
	}
}