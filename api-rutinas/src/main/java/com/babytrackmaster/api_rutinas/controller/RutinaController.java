package com.babytrackmaster.api_rutinas.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.babytrackmaster.api_rutinas.dto.RutinaCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionDTO;
import com.babytrackmaster.api_rutinas.service.RutinaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/rutinas")
@Tag(name = "Rutinas", description = "Gestión de rutinas y su historial de ejecuciones")
public class RutinaController {

        private final RutinaService rutinaService;

        public RutinaController(RutinaService rutinaService) {
                this.rutinaService = rutinaService;
        }

	// -------------------------------------------------------------------------
	// Crear
	// -------------------------------------------------------------------------
        @Operation(summary = "Crear una rutina", description = "Crea una nueva rutina para el usuario y bebé indicados")
        @PostMapping("/usuario/{usuarioId}/bebe/{bebeId}")
        public ResponseEntity<RutinaDTO> crear(
                        @PathVariable Long usuarioId,
                        @PathVariable Long bebeId,
                        @Valid @org.springframework.web.bind.annotation.RequestBody RutinaCreateDTO dto) {
                RutinaDTO res = rutinaService.crear(usuarioId, bebeId, dto);
                return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

	// -------------------------------------------------------------------------
	// Obtener por id
	// -------------------------------------------------------------------------
	@Operation(summary = "Obtener una rutina", description = "Devuelve una rutina por su identificador si pertenece al usuario.")
        @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}")
        public ResponseEntity<RutinaDTO> obtener(
                        @PathVariable Long usuarioId,
                        @PathVariable Long bebeId,
                        @Parameter(description = "ID de la rutina", example = "1") @PathVariable Long id) {
                return ResponseEntity.ok(rutinaService.obtener(usuarioId, bebeId, id));
        }

	// -------------------------------------------------------------------------
	// Actualizar
	// -------------------------------------------------------------------------
	@Operation(summary = "Actualizar una rutina", description = "Actualiza los datos de una rutina propia.")
        @PutMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}")
        public ResponseEntity<RutinaDTO> actualizar(
                        @PathVariable Long usuarioId,
                        @PathVariable Long bebeId,
                        @Parameter(description = "ID de la rutina", example = "1") @PathVariable Long id,
                        @Valid @org.springframework.web.bind.annotation.RequestBody RutinaCreateDTO dto) {
                return ResponseEntity.ok(rutinaService.actualizar(usuarioId, bebeId, id, dto));
        }

	// -------------------------------------------------------------------------
	// Eliminar
	// -------------------------------------------------------------------------
	@Operation(summary = "Eliminar una rutina", description = "Elimina una rutina propia.")
        @DeleteMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}")
        public ResponseEntity<Void> eliminar(
                        @PathVariable Long usuarioId,
                        @PathVariable Long bebeId,
                        @Parameter(description = "ID de la rutina", example = "1") @PathVariable Long id) {
                rutinaService.eliminar(usuarioId, bebeId, id);
                return ResponseEntity.noContent().build();
        }

	// -------------------------------------------------------------------------
    // Listar (con filtros y paginación)
    // -------------------------------------------------------------------------
    @Operation(
        summary = "Listar rutinas",
        description = "Lista las rutinas propias con filtros opcionales por activo y día, y con paginación/ordenación."
    )
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}")
    public ResponseEntity<Page<RutinaDTO>> listar(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @Parameter(description = "Filtra por estado activo/inactivo", example = "true")
            @RequestParam(required = false) Boolean activo,
            @Parameter(description = "Filtra por día. Puede ser letra (L,M,X,J,V,S,D) o número (1..7)", example = "L")
            @RequestParam(required = false) String dia,
            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Orden (campo,dir). Ej: id,desc o nombre,asc", example = "id,desc")
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
                String[] parts = sort.split(",");
                Sort.Direction dir = parts.length > 1 && "asc".equalsIgnoreCase(parts[1]) ? Sort.Direction.ASC
                                : Sort.Direction.DESC;
                Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(dir, parts[0])));
                Page<RutinaDTO> res = rutinaService.listar(usuarioId, bebeId, activo, dia, pageable);
                return ResponseEntity.ok(res);
        }

 // -------------------------------------------------------------------------
    // Activar / Desactivar
    // -------------------------------------------------------------------------
    @Operation(summary = "Activar rutina", description = "Marca una rutina como activa.")
    @PostMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}/activar")
    public ResponseEntity<RutinaDTO> activar(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @Parameter(description = "ID de la rutina", example = "1")
            @PathVariable Long id) {
                return ResponseEntity.ok(rutinaService.activar(usuarioId, bebeId, id));
        }

    @Operation(summary = "Desactivar rutina", description = "Marca una rutina como inactiva.")
    @PostMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}/desactivar")
    public ResponseEntity<RutinaDTO> desactivar(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @Parameter(description = "ID de la rutina", example = "1")
            @PathVariable Long id) {
                return ResponseEntity.ok(rutinaService.desactivar(usuarioId, bebeId, id));
        }

 // -------------------------------------------------------------------------
    // Registrar ejecución
    // -------------------------------------------------------------------------
    @Operation(summary = "Registrar ejecución", description = "Registra una ejecución para la rutina indicada.")
    @PostMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}/ejecuciones")
    public ResponseEntity<RutinaEjecucionDTO> registrarEjecucion(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @Parameter(description = "ID de la rutina", example = "1")
            @PathVariable Long id,
            @Valid
            @org.springframework.web.bind.annotation.RequestBody RutinaEjecucionCreateDTO dto) {
                return ResponseEntity.status(HttpStatus.CREATED).body(rutinaService.registrarEjecucion(usuarioId, bebeId, id, dto));
        }

 // -------------------------------------------------------------------------
    // Historial
    // -------------------------------------------------------------------------
    @Operation(summary = "Historial de ejecuciones", description = "Devuelve el historial de ejecuciones de una rutina en un rango de fechas (opcional).")
    @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}/{id}/ejecuciones")
    public ResponseEntity<java.util.List<RutinaEjecucionDTO>> historial(
            @PathVariable Long usuarioId,
            @PathVariable Long bebeId,
            @Parameter(description = "ID de la rutina", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Fecha desde (incluida) en formato YYYY-MM-DD", example = "2025-08-01")
            @RequestParam(required = false) String desde,
            @Parameter(description = "Fecha hasta (incluida) en formato YYYY-MM-DD", example = "2025-08-31")
            @RequestParam(required = false) String hasta) {
                LocalDate d = (desde != null && desde.length() > 0) ? LocalDate.parse(desde) : null;
                LocalDate h = (hasta != null && hasta.length() > 0) ? LocalDate.parse(hasta) : null;
                return ResponseEntity.ok(rutinaService.historial(usuarioId, bebeId, id, d, h));
        }
}
