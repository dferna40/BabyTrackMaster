package com.babytrackmaster.api_hitos.controller;

import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
import com.babytrackmaster.api_hitos.service.HitoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hitos")
@RequiredArgsConstructor
public class HitoController {

    private final HitoService service;

    private Long getUsuarioId(Authentication auth) {
        // En el filtro guardamos el sub (userId) como principal (String)
        String sub = (String) auth.getPrincipal();
        return Long.valueOf(sub);
    }

    @Operation(summary = "Crear hito")
    @PostMapping
    public HitoResponse crear(Authentication auth, @Valid @RequestBody HitoRequest request) {
        return service.crear(getUsuarioId(auth), request);
    }

    @Operation(summary = "Actualizar hito")
    @PutMapping("/{id}")
    public HitoResponse actualizar(Authentication auth, @PathVariable Long id, @Valid @RequestBody HitoRequest request) {
        return service.actualizar(getUsuarioId(auth), id, request);
    }

    @Operation(summary = "Eliminar hito")
    @DeleteMapping("/{id}")
    public void eliminar(Authentication auth, @PathVariable Long id) {
        service.eliminar(getUsuarioId(auth), id);
    }

    @Operation(summary = "Obtener hito por id")
    @GetMapping("/{id}")
    public HitoResponse obtener(Authentication auth, @PathVariable Long id) {
        return service.obtener(getUsuarioId(auth), id);
    }

    @Operation(summary = "Listar todos los hitos del usuario")
    @GetMapping
    public List<HitoResponse> listar(Authentication auth) {
        return service.listar(getUsuarioId(auth));
    }

    @Operation(summary = "Listar por bebé")
    @GetMapping("/bebe/{bebeId}")
    public List<HitoResponse> listarPorBebe(Authentication auth, @PathVariable Long bebeId) {
        return service.listarPorBebe(getUsuarioId(auth), bebeId);
    }

    @Operation(summary = "Listar por mes (YYYY-MM)")
    @GetMapping("/mes/{yyyyMM}")
    public List<HitoResponse> listarPorMes(Authentication auth, @PathVariable("yyyyMM") @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
        return service.listarPorMes(getUsuarioId(auth), mes);
    }

    @Operation(summary = "Buscar por título (contiene)")
    @GetMapping("/buscar")
    public List<HitoResponse> buscarPorTitulo(Authentication auth, @RequestParam("q") String q) {
        return service.buscarPorTitulo(getUsuarioId(auth), q);
    }
}