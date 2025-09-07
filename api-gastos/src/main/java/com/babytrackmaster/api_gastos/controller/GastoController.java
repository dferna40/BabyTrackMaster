package com.babytrackmaster.api_gastos.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_gastos.dto.GastoCreateRequest;
import com.babytrackmaster.api_gastos.dto.GastoResponse;
import com.babytrackmaster.api_gastos.dto.GastoUpdateRequest;
import com.babytrackmaster.api_gastos.dto.ResumenMensualResponse;
import com.babytrackmaster.api_gastos.service.GastoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gastos")
@RequiredArgsConstructor
@Tag(name = "Gastos", description = "Gestión de gastos y consultas por mes/categoría")
public class GastoController {

        private final GastoService gastoService;

        @Operation(summary = "Crear un gasto", description = "Crea un nuevo gasto para un usuario específico")
        @PostMapping("/usuario/{usuarioId}")
        public ResponseEntity<GastoResponse> crear(
                        @PathVariable Long usuarioId,
                        @Valid @RequestBody GastoCreateRequest req) {
                GastoResponse resp = gastoService.crear(usuarioId, req);
                return new ResponseEntity<GastoResponse>(resp, HttpStatus.CREATED);
        }

        @Operation(summary = "Actualizar un gasto", description = "Actualiza los datos de un gasto propio")
        @PutMapping("/usuario/{usuarioId}/{id}")
        public ResponseEntity<GastoResponse> actualizar(
                        @PathVariable Long usuarioId,
                        @PathVariable Long id,
                        @Valid @RequestBody GastoUpdateRequest req) {
                GastoResponse resp = gastoService.actualizar(usuarioId, id, req);
                return ResponseEntity.ok(resp);
        }

        @Operation(summary = "Eliminar un gasto", description = "Elimina un gasto propio")
        @DeleteMapping("/usuario/{usuarioId}/{id}")
        public ResponseEntity<Void> eliminar(
                        @PathVariable Long usuarioId,
                        @PathVariable Long id) {
                gastoService.eliminar(usuarioId, id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Obtener un gasto", description = "Devuelve un gasto propio por su ID")
        @GetMapping("/usuario/{usuarioId}/{id}")
        public ResponseEntity<GastoResponse> obtener(
                        @PathVariable Long usuarioId,
                        @PathVariable Long id) {
                return ResponseEntity.ok(gastoService.obtener(usuarioId, id));
        }

	// === Listados/consultas ===

        @Operation(summary = "Listar gastos por mes", description = "Lista paginada de gastos del usuario para un año y mes dados, ordenada por fecha descendente.")
        @GetMapping("/usuario/{usuarioId}/mes")
        public ResponseEntity<Page<GastoResponse>> listarPorMes(
                        @PathVariable Long usuarioId,
                        @RequestParam int anio,
                        @RequestParam int mes,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fecha")));
                Page<GastoResponse> resp = gastoService.listarPorMes(usuarioId, anio, mes, pageable);
                return ResponseEntity.ok(resp);
        }

        @Operation(summary = "Listar gastos por categoría", description = "Lista paginada de gastos del usuario filtrando por categoría, ordenada por fecha descendente.")
        @GetMapping("/usuario/{usuarioId}/categoria/{categoriaId}")
        public ResponseEntity<Page<GastoResponse>> listarPorCategoria(
                        @PathVariable Long usuarioId,
                        @PathVariable Long categoriaId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fecha")));
                Page<GastoResponse> resp = gastoService.listarPorCategoria(usuarioId, categoriaId, pageable);
                return ResponseEntity.ok(resp);
        }

        @Operation(summary = "Listar gastos por bebé", description = "Lista paginada de gastos del usuario filtrando por bebé, ordenada por fecha descendente.")
        @GetMapping("/usuario/{usuarioId}/bebe/{bebeId}")
        public ResponseEntity<Page<GastoResponse>> listarPorBebe(
                        @PathVariable Long usuarioId,
                        @PathVariable Long bebeId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fecha")));
                Page<GastoResponse> resp = gastoService.listarPorBebe(usuarioId, bebeId, pageable);
                return ResponseEntity.ok(resp);
        }

        @Operation(
                summary = "Obtener resumen mensual",
                description = "Totales por categoría y total del mes para el usuario."
            )
        @GetMapping("/usuario/{usuarioId}/resumen")
        public ResponseEntity<ResumenMensualResponse> resumenMensual(
                        @PathVariable Long usuarioId,
                        @RequestParam int anio,
                        @RequestParam int mes) {
                ResumenMensualResponse resp = gastoService.resumenMensual(usuarioId, anio, mes);
                return ResponseEntity.ok(resp);
        }
}