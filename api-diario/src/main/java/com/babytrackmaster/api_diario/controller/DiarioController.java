package com.babytrackmaster.api_diario.controller;

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

import com.babytrackmaster.api_diario.dto.DiarioCreateDTO;
import com.babytrackmaster.api_diario.dto.DiarioResponseDTO;
import com.babytrackmaster.api_diario.dto.DiarioUpdateDTO;
import com.babytrackmaster.api_diario.dto.PageResponseDTO;
import com.babytrackmaster.api_diario.service.DiarioService;
import java.util.List;

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

        @Operation(summary = "Crear entrada de diario")
        @ApiResponse(responseCode = "201", description = "Creado")
        @PostMapping("/usuario/{usuarioId}")
        public ResponseEntity<DiarioResponseDTO> crear(
                        @PathVariable Long usuarioId,
                        @Valid @RequestBody DiarioCreateDTO dto) {
                DiarioResponseDTO resp = service.crear(usuarioId, dto);
                return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        }

        @Operation(summary = "Obtener entrada por id")
        @GetMapping("/usuario/{usuarioId}/{id}")
        public ResponseEntity<DiarioResponseDTO> obtener(
                        @PathVariable Long usuarioId,
                        @PathVariable Long id) {
                return ResponseEntity.ok(service.obtener(usuarioId, id));
        }

        @Operation(summary = "Actualizar entrada por id")
        @PutMapping("/usuario/{usuarioId}/{id}")
        public ResponseEntity<DiarioResponseDTO> actualizar(
                        @PathVariable Long usuarioId,
                        @PathVariable Long id,
                        @Valid @RequestBody DiarioUpdateDTO dto) {
                return ResponseEntity.ok(service.actualizar(usuarioId, id, dto));
        }

        @Operation(summary = "Eliminar (lógico) entrada por id")
        @DeleteMapping("/usuario/{usuarioId}/{id}")
        public ResponseEntity<Void> eliminar(
                        @PathVariable Long usuarioId,
                        @PathVariable Long id) {
                service.eliminar(usuarioId, id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Listar por rango de fechas (incluidos)")
        @GetMapping("/usuario/{usuarioId}")
        public ResponseEntity<PageResponseDTO<DiarioResponseDTO>> listarRango(
                        @PathVariable Long usuarioId,
                        @RequestParam String desde, @RequestParam String hasta,
                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
                return ResponseEntity.ok(service.listarRango(usuarioId, desde, hasta, page, size));
        }

        @Operation(summary = "Listar por día")
        @GetMapping("/usuario/{usuarioId}/dia/{fecha}")
        public ResponseEntity<PageResponseDTO<DiarioResponseDTO>> listarDia(
                        @PathVariable Long usuarioId,
                        @PathVariable String fecha,
                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
                return ResponseEntity.ok(service.listarDia(usuarioId, fecha, page, size));
        }

        @Operation(summary = "Listar por etiqueta (contiene)")
        @GetMapping("/usuario/{usuarioId}/tags")
        public ResponseEntity<PageResponseDTO<DiarioResponseDTO>> listarPorTag(
                        @PathVariable Long usuarioId,
                        @RequestParam(required = false) String tag,
                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
                return ResponseEntity.ok(service.listarPorTag(usuarioId, tag, page, size));
        }

        @Operation(summary = "Listar entradas por bebé")
        @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}")
        public ResponseEntity<List<DiarioResponseDTO>> listarPorBebe(
                        @PathVariable Long usuarioId,
                        @PathVariable Long bebeId) {
                return ResponseEntity.ok(service.listarPorBebe(usuarioId, bebeId));
        }
}
