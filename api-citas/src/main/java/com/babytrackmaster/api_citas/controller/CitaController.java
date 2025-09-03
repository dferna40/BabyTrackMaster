package com.babytrackmaster.api_citas.controller;

import com.babytrackmaster.api_citas.dto.*;
import com.babytrackmaster.api_citas.security.JwtService;
import com.babytrackmaster.api_citas.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Citas", description = "Gestión de citas médicas del bebé")
@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
public class CitaController {

	private final CitaService service;
	private final JwtService jwtService; // -> clase que obtenga usuarioId del token (igual que en otros servicios)

	@Operation(summary = "Crear cita")
	@PostMapping
	public ResponseEntity<CitaResponseDTO> crear(@Valid @RequestBody CitaCreateDTO dto) {
		Long usuarioId = jwtService.resolveUserId();
		return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto, usuarioId));
	}

	@Operation(summary = "Actualizar cita")
	@PutMapping("/{id}")
	public ResponseEntity<CitaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CitaUpdateDTO dto) {
		Long usuarioId = jwtService.resolveUserId();
		return ResponseEntity.ok(service.actualizar(id, usuarioId, dto));
	}

	@Operation(summary = "Eliminar cita (lógico)")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Long usuarioId = jwtService.resolveUserId();
		service.eliminar(id, usuarioId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Obtener cita por id")
	@GetMapping("/{id}")
	public ResponseEntity<CitaResponseDTO> obtener(@PathVariable Long id) {
		Long usuarioId = jwtService.resolveUserId();
		return ResponseEntity.ok(service.obtener(id, usuarioId));
	}

	@Operation(summary = "Listar por rango de fechas (orden ascendente)")
	@GetMapping
	public ResponseEntity<Page<CitaResponseDTO>> listarRango(@RequestParam String desde, @RequestParam String hasta,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Long usuarioId = jwtService.resolveUserId();
		return ResponseEntity.ok(service.listarRango(usuarioId, desde, hasta, page, size));
	}

        @Operation(summary = "Listar por estado")
        @GetMapping("/estado/{estadoId}")
        public ResponseEntity<Page<CitaResponseDTO>> listarPorEstado(@PathVariable Long estadoId,
                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
                Long usuarioId = jwtService.resolveUserId();
                return ResponseEntity.ok(service.listarPorEstado(usuarioId, estadoId, page, size));
        }

        @Operation(summary = "Listar por tipo")
        @GetMapping("/tipo/{tipoId}")
        public ResponseEntity<Page<CitaResponseDTO>> listarPorTipo(@PathVariable Long tipoId,
                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
                Long usuarioId = jwtService.resolveUserId();
                return ResponseEntity.ok(service.listarPorTipo(usuarioId, tipoId, page, size));
        }

        @Operation(summary = "Listar por médico")
        @GetMapping("/medico")
        public ResponseEntity<Page<CitaResponseDTO>> listarPorMedico(@RequestParam String nombre,
                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
                Long usuarioId = jwtService.resolveUserId();
                return ResponseEntity.ok(service.listarPorMedico(usuarioId, nombre, page, size));
        }

        @Operation(summary = "Listar por bebé")
        @GetMapping("/bebe/{bebeId}")
        public ResponseEntity<Page<CitaResponseDTO>> listarPorBebe(@PathVariable Long bebeId,
                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
                Long usuarioId = jwtService.resolveUserId();
                return ResponseEntity.ok(service.listarPorBebe(usuarioId, bebeId, page, size));
        }
}
