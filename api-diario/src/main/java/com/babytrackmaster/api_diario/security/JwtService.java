package com.babytrackmaster.api_diario.security;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    // Secret en Base64 (recomendado). Ejemplo YAML:
    // security.jwt.secret: dGhpc19pc19hX3NhZmVfZGV2X3NlY3JldF9iYXNlNjQ=
	@Value("${security.jwt.secret}")
    private String secretB64;

    // Expiración en milisegundos. Ejemplo YAML:
    // security.jwt.expiration-ms: 259200000
    @Value("${security.jwt.expiration-ms:86400000}")
    private long expirationMs;

    // Skew de reloj permitido (en segundos) para validar exp/iat (tolerancia)
    private static final long CLOCK_SKEW_SECONDS = 30L;

    /**
     * Obtiene todas las claims, validando la firma.
     * Lanza excepción si el token es inválido o ha sido manipulado.
     */
    public Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .setAllowedClockSkewSeconds(CLOCK_SKEW_SECONDS)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Indica si el token es válido (firma correcta y no expirado).
     * NO lanza excepción: devuelve false ante cualquier problema.
     */
    public boolean esTokenValido(String token) {
        try {
            Claims claims = getAllClaims(token);
            Date exp = claims.getExpiration();
            if (exp == null) {
                // Si no trae exp, lo consideramos inválido para no abrir agujeros
                return false;
            }
            return exp.after(new Date());
        } catch (Exception ex) {
            // Firma inválida, token corrupto o expirado, etc.
            return false;
        }
    }

    /**
     * (Opcional) Obtiene el "sub" (suele ser username o id) sin lambdas.
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims c = getAllClaims(token);
            return c.getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * (Opcional) Genera un JWT sencillo con subject y expiración.
     * Útil si en este servicio quisieras emitir tokens (normalmente sólo valida).
     */
    public String generarToken(String subject) {
        Date ahora = new Date();
        Date exp = new Date(ahora.getTime() + expirationMs);

        return Jwts
                .builder()
                .setSubject(subject)
                .setIssuedAt(ahora)
                .setExpiration(exp)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Construye la clave de firma HS256 a partir del secreto.
     * Intenta decodificar Base64; si falla, usa los bytes "tal cual".
     */
    private Key getSigningKey() {
        byte[] keyBytes;
        try {
            // Preferimos Base64 (coincide con tu YAML actual)
            keyBytes = Decoders.BASE64.decode(secretB64);
        } catch (IllegalArgumentException ex) {
            // Si no era Base64, usamos el texto sin decodificar
            keyBytes = secretB64.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        // Requiere >= 32 bytes para HS256
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
 // Intenta obtener el userId del SecurityContext y, si no puede, del JWT en el header.
    public Long resolveUserId() {
        // 1) SecurityContext: principal.getId(), details["userId"], auth.getName()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // principal.getId()
            Object principal = auth.getPrincipal();
            if (principal != null) {
                try {
                    java.lang.reflect.Method m = principal.getClass().getMethod("getId", new Class[] {});
                    Object val = m.invoke(principal, new Object[] {});
                    if (val != null) {
                        try {
                            return Long.valueOf(String.valueOf(val));
                        } catch (NumberFormatException ignore) { /* seguimos */ }
                    }
                } catch (Exception ignore) { /* seguimos */ }
            }
            // details["userId"]
            Object details = auth.getDetails();
            if (details instanceof Map) {
                Map map = (Map) details;
                Object v = map.get("userId");
                if (v != null) {
                    try { return Long.valueOf(String.valueOf(v)); }
                    catch (NumberFormatException ignore) { /* seguimos */ }
                }
            }
            // auth.getName() numérico
            String name = auth.getName();
            if (name != null) {
                try { return Long.valueOf(name); }
                catch (NumberFormatException ignore) { /* seguimos */ }
            }
        }

        // 2) Fallback: leer Authorization: Bearer ... y extraer de las claims (plan A: claves típicas; plan B: escaneo)
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest req = attrs.getRequest();
                if (req != null) {
                    String header = req.getHeader("Authorization");
                    if (header != null && header.startsWith("Bearer ")) {
                        String token = header.substring(7);
                        if (esTokenValido(token)) {
                            Claims claims = getAllClaims(token);
                            Long id = extractUserIdFromClaims(claims);
                            if (id != null) {
                                return id;
                            }
                            // Último intento: subject numérico
                            String sub = claims.getSubject();
                            if (sub != null) {
                                try { return Long.valueOf(sub); }
                                catch (NumberFormatException ignore) { /* nada */ }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignore) { /* no interrumpimos */ }

        return null; // no se pudo
    }

    // === MÉTODO PRIVADO NUEVO ===
    // Intenta obtener userId de claims: primero por claves conocidas, luego escaneo profundo (incluye objetos anidados y colecciones)
    private Long extractUserIdFromClaims(Claims claims) {
        if (claims == null || claims.isEmpty()) {
            return null;
        }

        // 1) Claves típicas directas
        String[] keys = new String[] { "userId", "usuarioId", "id", "uid", "user_id", "usuario_id" };
        int i = 0;
        while (i < keys.length) {
            Object v = claims.get(keys[i]);
            Long parsed = tryParseLong(v);
            if (parsed != null) {
                return parsed;
            }
            i++;
        }

        // 2) Escaneo profundo de todo el mapa de claims (por si viene anidado tipo { "user": { "id": 1 } })
        return deepScanForId(claims);
    }

    // === MÉTODOS AUXILIARES PRIVADOS NUEVOS ===

    private Long tryParseLong(Object v) {
        if (v == null) return null;
        if (v instanceof Number) {
            return Long.valueOf(((Number) v).longValue());
        }
        String s = String.valueOf(v);
        if (s == null) return null;
        s = s.trim();
        if (s.length() == 0) return null;
        int j = 0;
        boolean negativo = false;
        if (s.charAt(0) == '-') {
            negativo = true;
            j = 1;
        }
        while (j < s.length()) {
            char c = s.charAt(j);
            if (c < '0' || c > '9') return null;
            j++;
        }
        try { return Long.valueOf(s); } catch (NumberFormatException e) { return null; }
    }

    // Busca un número en estructuras anidadas (Map/Collection/Object).
    private Long deepScanForId(Object obj) {
        if (obj == null) return null;

        // Si es número directo, devuélvelo
        if (obj instanceof Number) {
            return Long.valueOf(((Number) obj).longValue());
        }

        // Si es texto con sólo dígitos (o -), parsea
        if (obj instanceof String) {
            return tryParseLong(obj);
        }

        // Si es Map: mira primero claves que contengan "id", luego sigue escaneando valores
        if (obj instanceof Map) {
            Map map = (Map) obj;
            Set entrySet = map.entrySet();
            Iterator it = entrySet.iterator();
            // 2.1) prioridades: claves que contengan "id"
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                Object k = e.getKey();
                Object v = e.getValue();
                if (k != null) {
                    String ks = String.valueOf(k).toLowerCase();
                    if (ks.indexOf("id") >= 0) {
                        Long parsed = tryParseLong(v);
                        if (parsed != null) {
                            return parsed;
                        }
                    }
                }
            }
            // 2.2) si no, escanea valores recursivamente
            it = entrySet.iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                Object v = e.getValue();
                Long found = deepScanForId(v);
                if (found != null) return found;
            }
            return null;
        }

        // Si es Collection: recorre elementos
        if (obj instanceof Collection) {
            Collection col = (Collection) obj;
            Iterator it = col.iterator();
            while (it.hasNext()) {
                Object v = it.next();
                Long found = deepScanForId(v);
                if (found != null) return found;
            }
            return null;
        }

        // Si es POJO: no vamos a reflejar todo el objeto; devolvemos null
        return null;
    }
}