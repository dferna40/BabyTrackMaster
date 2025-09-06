package com.babytrackmaster.api_alimentacion.security;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_ROLE  = "role";

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration-ms:86400000}")
    private long expirationMs;

    /* ========== Validación / Lectura ========== */

    public boolean isTokenValid(String token) {
        try {
            Claims c = extractAllClaims(token);
            return c.getExpiration() != null && c.getExpiration().after(new Date());
        } catch (Exception e) {
            log.debug("[JWT] Token inválido: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);

        Object rolesObj = claims.get(CLAIM_ROLES);
        if (rolesObj instanceof Collection<?> col) {
            return col.stream()
                    .map(Objects::toString)
                    .filter(s -> !s.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        Object role = claims.get(CLAIM_ROLE);
        if (role instanceof String r && !r.isBlank()) {
            return List.of(new SimpleGrantedAuthority(r));
        }

        return List.of();
    }

    public Claims extractAllClaims(String token) {
        String compact = stripBearer(token);
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(compact)
                .getBody();
    }

    // Intenta resolver el usuarioId desde el contexto de seguridad o el JWT
    public Long resolveUserId() {
        // 1) Revisar el SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // principal.getId()
            Object principal = auth.getPrincipal();
            if (principal != null) {
                try {
                    java.lang.reflect.Method m = principal.getClass().getMethod("getId");
                    Object val = m.invoke(principal);
                    if (val != null) {
                        try { return Long.valueOf(String.valueOf(val)); }
                        catch (NumberFormatException ignore) { }
                    }
                } catch (Exception ignore) { }
            }
            // details["userId"]
            Object details = auth.getDetails();
            if (details instanceof Map<?,?> map) {
                Object v = map.get("userId");
                if (v != null) {
                    try { return Long.valueOf(String.valueOf(v)); }
                    catch (NumberFormatException ignore) { }
                }
            }
            // auth.getName()
            String name = auth.getName();
            if (name != null) {
                try { return Long.valueOf(name); }
                catch (NumberFormatException ignore) { }
            }
        }

        // 2) Fallback: leer Authorization: Bearer ...
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest req = attrs.getRequest();
                if (req != null) {
                    String header = req.getHeader("Authorization");
                    if (header != null && header.startsWith("Bearer ")) {
                        String token = header.substring(7);
                        if (isTokenValid(token)) {
                            Claims claims = extractAllClaims(token);
                            Long id = extractUserIdFromClaims(claims);
                            if (id != null) {
                                return id;
                            }
                            String sub = claims.getSubject();
                            if (sub != null) {
                                try { return Long.valueOf(sub); }
                                catch (NumberFormatException ignore) { }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignore) { }

        return null;
    }

    // Busca userId en las claims del token
    private Long extractUserIdFromClaims(Claims claims) {
        if (claims == null || claims.isEmpty()) return null;

        String[] keys = { "userId", "usuarioId", "id", "uid", "user_id", "usuario_id" };
        for (String k : keys) {
            Object v = claims.get(k);
            Long parsed = tryParseLong(v);
            if (parsed != null) return parsed;
        }

        return deepScanForId(claims);
    }

    private Long tryParseLong(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) {
            return n.longValue();
        }
        String s = String.valueOf(v);
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        try { return Long.valueOf(s); }
        catch (NumberFormatException e) { return null; }
    }

    private Long deepScanForId(Object obj) {
        if (obj == null) return null;

        if (obj instanceof Map<?,?> map) {
            // primero claves que contengan "id"
            for (Map.Entry<?,?> e : map.entrySet()) {
                Object k = e.getKey();
                if (k != null) {
                    String ks = String.valueOf(k).toLowerCase();
                    if (ks.contains("id")) {
                        Long parsed = tryParseLong(e.getValue());
                        if (parsed != null) return parsed;
                    }
                }
            }
            // luego escanear valores
            for (Map.Entry<?,?> e : map.entrySet()) {
                Long found = deepScanForId(e.getValue());
                if (found != null) return found;
            }
            return null;
        }

        if (obj instanceof Collection<?> col) {
            for (Object v : col) {
                Long found = deepScanForId(v);
                if (found != null) return found;
            }
            return null;
        }

        return tryParseLong(obj);
    }

    /* ========== (Opcional) Generación para pruebas locales ========== */

    public String generateToken(String subject, Collection<?> roles) {
        Map<String, Object> claims = new HashMap<>();
        if (roles != null && !roles.isEmpty()) {
            claims.put(CLAIM_ROLES, roles.stream().map(Object::toString).toList());
        }
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* ========== Utils ========== */

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String stripBearer(String token) {
        if (token == null) return "";
        token = token.trim();
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}