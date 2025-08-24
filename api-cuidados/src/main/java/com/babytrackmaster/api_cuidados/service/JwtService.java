package com.babytrackmaster.api_cuidados.service;

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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

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