package com.babytrackmaster.api_rutinas.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.babytrackmaster.api_rutinas.dto.RutinaCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionCreateDTO;
import com.babytrackmaster.api_rutinas.dto.RutinaEjecucionDTO;
import com.babytrackmaster.api_rutinas.security.JwtService;
import com.babytrackmaster.api_rutinas.service.RutinaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/rutinas")
public class RutinaController {

    @Autowired
    private RutinaService rutinaService;
    
    @Autowired
    private JwtService jwtService;

    // TODO: reemplaza por tu servicio real de JWT/seguridad
    private Long getUsuarioIdDesdeToken() {
        // En tu proyecto real: extraer del SecurityContext/claims.
        // Aquí dejamos un “stub” que debe ser reemplazado.
        return jwtService.resolveUserId(); 
    }

    @PostMapping
    public ResponseEntity<RutinaDTO> crear(@Valid @RequestBody RutinaCreateDTO dto) {
        Long usuarioId = getUsuarioIdDesdeToken();
        RutinaDTO res = rutinaService.crear(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaDTO> obtener(@PathVariable Long id) {
        Long usuarioId = getUsuarioIdDesdeToken();
        return ResponseEntity.ok(rutinaService.obtener(usuarioId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RutinaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RutinaCreateDTO dto) {
        Long usuarioId = getUsuarioIdDesdeToken();
        return ResponseEntity.ok(rutinaService.actualizar(usuarioId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Long usuarioId = getUsuarioIdDesdeToken();
        rutinaService.eliminar(usuarioId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<RutinaDTO>> listar(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String dia, // "L" o "1"
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        Long usuarioId = getUsuarioIdDesdeToken();
        String[] parts = sort.split(",");
        Sort.Direction dir = parts.length > 1 && "asc".equalsIgnoreCase(parts[1]) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(dir, parts[0])));
        Page<RutinaDTO> res = rutinaService.listar(usuarioId, activo, dia, pageable);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{id}/activar")
    public ResponseEntity<RutinaDTO> activar(@PathVariable Long id) {
        Long usuarioId = getUsuarioIdDesdeToken();
        return ResponseEntity.ok(rutinaService.activar(usuarioId, id));
    }

    @PostMapping("/{id}/desactivar")
    public ResponseEntity<RutinaDTO> desactivar(@PathVariable Long id) {
        Long usuarioId = getUsuarioIdDesdeToken();
        return ResponseEntity.ok(rutinaService.desactivar(usuarioId, id));
    }

    @PostMapping("/{id}/ejecuciones")
    public ResponseEntity<RutinaEjecucionDTO> registrarEjecucion(@PathVariable Long id,
                                                                 @Valid @RequestBody RutinaEjecucionCreateDTO dto) {
        Long usuarioId = getUsuarioIdDesdeToken();
        return ResponseEntity.status(HttpStatus.CREATED).body(rutinaService.registrarEjecucion(usuarioId, id, dto));
    }

    @GetMapping("/{id}/ejecuciones")
    public ResponseEntity<java.util.List<RutinaEjecucionDTO>> historial(@PathVariable Long id,
                                                                        @RequestParam(required=false) String desde,
                                                                        @RequestParam(required=false) String hasta) {
        Long usuarioId = getUsuarioIdDesdeToken();
        LocalDate d = (desde != null && desde.length() > 0) ? LocalDate.parse(desde) : null;
        LocalDate h = (hasta != null && hasta.length() > 0) ? LocalDate.parse(hasta) : null;
        return ResponseEntity.ok(rutinaService.historial(usuarioId, id, d, h));
    }
}
