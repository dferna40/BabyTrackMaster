package com.babytrackmaster.api_gastos.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.babytrackmaster.api_gastos.dto.*;
import com.babytrackmaster.api_gastos.service.GastoService;

@RestController
@RequestMapping("/api/v1/gastos")
public class GastoController {

    @Autowired
    private GastoService gastoService;

//    // Nota: usuarioId lo obtendrás del JWT (por ejemplo, inyectado por un filtro)
//    private Long getUsuarioIdFromContext() {
//        // Implementa como en api-cuidados (reutiliza tu JwtService/Filtro).
//        // Placeholder:
//        return JwtContextHolder.getUserId(); // crea un helper similar al que ya usas
//    }
//
//    @PostMapping
//    public ResponseEntity<GastoResponse> crear(@Valid @RequestBody GastoCreateRequest req) {
//        Long userId = getUsuarioIdFromContext();
//        GastoResponse resp = gastoService.crear(userId, req);
//        return new ResponseEntity<GastoResponse>(resp, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<GastoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody GastoUpdateRequest req) {
//        Long userId = getUsuarioIdFromContext();
//        GastoResponse resp = gastoService.actualizar(userId, id, req);
//        return ResponseEntity.ok(resp);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
//        Long userId = getUsuarioIdFromContext();
//        gastoService.eliminar(userId, id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<GastoResponse> obtener(@PathVariable Long id) {
//        Long userId = getUsuarioIdFromContext();
//        return ResponseEntity.ok(gastoService.obtener(userId, id));
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<GastoResponse>> listarPorMes(
//            @RequestParam int anio,
//            @RequestParam int mes,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size,
//            @RequestParam(defaultValue = "fecha,desc") String sort
//    ) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fecha")));
//        Long userId = getUsuarioIdFromContext();
//        Page<GastoResponse> resp = gastoService.listarPorMes(userId, anio, mes, pageable);
//        return ResponseEntity.ok(resp);
//    }
//
//    @GetMapping("/categoria/{categoriaId}")
//    public ResponseEntity<Page<GastoResponse>> listarPorCategoria(
//            @PathVariable Long categoriaId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size
//    ) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fecha")));
//        Long userId = getUsuarioIdFromContext();
//        return ResponseEntity.ok(gastoService.listarPorCategoria(userId, categoriaId, pageable));
//    }
//
//    @GetMapping("/resumen")
//    public ResponseEntity<ResumenMensualResponse> resumenMensual(
//            @RequestParam int anio,
//            @RequestParam int mes
//    ) {
//        Long userId = getUsuarioIdFromContext();
//        return ResponseEntity.ok(gastoService.resumenMensual(userId, anio, mes));
//    }
}