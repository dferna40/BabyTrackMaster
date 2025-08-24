package com.babytrackmaster.api_gastos.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import com.babytrackmaster.api_gastos.dto.*;
import com.babytrackmaster.api_gastos.security.JwtService;
import com.babytrackmaster.api_gastos.service.GastoService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;
    private final JwtService jwtService;

    // === CRUD básico (mismo estilo que api-cuidados) ===
    @Operation(summary = "Crear un gasto")
    @PostMapping
    public ResponseEntity<GastoResponse> crear(@Valid @RequestBody GastoCreateRequest req) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        GastoResponse resp = gastoService.crear(userId, req);
        return new ResponseEntity<GastoResponse>(resp, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Actualizar un gasto")
    @PutMapping("/{id}")
    public ResponseEntity<GastoResponse> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody GastoUpdateRequest req) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        GastoResponse resp = gastoService.actualizar(userId, id, req);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Eliminar un gasto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        gastoService.eliminar(userId, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener un gasto")
    @GetMapping("/{id}")
    public ResponseEntity<GastoResponse> obtener(@PathVariable Long id) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(gastoService.obtener(userId, id));
    }

    // === Listados/consultas (mantengo tus firmas y lógica de api-gastos, pero con estilo de api-cuidados) ===

    @Operation(summary = "Listar gastos por mes")
    @GetMapping("/mes")
    public ResponseEntity<Page<GastoResponse>> listarPorMes(@RequestParam int anio,
                                                            @RequestParam int mes,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fecha")));
        Page<GastoResponse> resp = gastoService.listarPorMes(userId, anio, mes, pageable);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Listar gastos por categoria")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Page<GastoResponse>> listarPorCategoria(@PathVariable Long categoriaId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("fecha")));
        Page<GastoResponse> resp = gastoService.listarPorCategoria(userId, categoriaId, pageable);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Obtener resumen de gastos mensuales")
    @GetMapping("/resumen")
    public ResponseEntity<ResumenMensualResponse> resumenMensual(@RequestParam int anio,
                                                                 @RequestParam int mes) {
        Long userId = jwtService.resolveUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ResumenMensualResponse resp = gastoService.resumenMensual(userId, anio, mes);
        return ResponseEntity.ok(resp);
    }

    
    // === Helper para extraer el userId del contexto de seguridad (robusto y sin lambdas) ===
//    private Long getUsuarioIdFromContext() {
//        org.springframework.security.core.Authentication auth =
//                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            // 1) Intento con principal que tenga getId()
//            Object principal = auth.getPrincipal();
//            if (principal != null) {
//                try {
//                    java.lang.reflect.Method m = principal.getClass().getMethod("getId", new Class[] {});
//                    Object val = m.invoke(principal, new Object[] {});
//                    if (val != null) {
//                        return Long.valueOf(String.valueOf(val));
//                    }
//                } catch (Exception ignore) {
//                    // seguimos probando otras rutas
//                }
//            }
//
//            // 2) Intento con details como Map con "userId"
//            Object details = auth.getDetails();
//            if (details instanceof java.util.Map) {
//                java.util.Map map = (java.util.Map) details;
//                Object v = map.get("userId");
//                if (v != null) {
//                    try {
//                        return Long.valueOf(String.valueOf(v));
//                    } catch (NumberFormatException e) {
//                        // seguimos probando
//                    }
//                }
//            }
//
//            // 3) Intento con auth.getName() numérico (por si el sub es el id)
//            String name = auth.getName();
//            if (name != null) {
//                try {
//                    return Long.valueOf(name);
//                } catch (NumberFormatException e) {
//                    // seguimos probando
//                }
//            }
//        }
//
//        // 4) Fallback: leer el Bearer y sacar userId de las claims
//        try {
//            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if (attrs != null) {
//                HttpServletRequest req = attrs.getRequest();
//                if (req != null) {
//                    String header = req.getHeader("Authorization");
//                    if (header != null && header.startsWith("Bearer ")) {
//                        String token = header.substring(7);
//                        if (jwtService.esTokenValido(token)) {
//                            io.jsonwebtoken.Claims claims = jwtService.getAllClaims(token);
//
//                            // claves típicas donde suele viajar el id
//                            String[] keys = new String[] { "userId", "usuarioId", "id", "uid", "user_id" };
//                            int i = 0;
//                            while (i < keys.length) {
//                                Object val = claims.get(keys[i]);
//                                if (val != null) {
//                                    try {
//                                        return Long.valueOf(String.valueOf(val));
//                                    } catch (NumberFormatException e) {
//                                        // probamos siguiente clave
//                                    }
//                                }
//                                i++;
//                            }
//
//                            // Por último, si el sub es numérico
//                            String sub = claims.getSubject();
//                            if (sub != null) {
//                                try {
//                                    return Long.valueOf(sub);
//                                } catch (NumberFormatException e) {
//                                    // sub no era un id numérico
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception ignore) {
//            // si algo falla al leer/parsing, caeremos al error final
//        }
//
//        // Si llegamos aquí, no hemos podido determinar el id
//        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se pudo determinar el usuarioId del token.");
//    }
}