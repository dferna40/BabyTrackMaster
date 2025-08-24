package com.babytrackmaster.api_gastos.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${security.jwt.secret}")
    private String secret; // misma clave que en api-usuarios y api-cuidados

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean esTokenValido(String token) {
        try {
            Claims claims = getAllClaims(token);
            Date exp = claims.getExpiration();
            return exp != null && exp.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = getAllClaims(token);
        // suponiendo que en el token guardas userId en "sub" o en "userId"
        Object userIdClaim = claims.get("userId");
        if (userIdClaim == null) {
            // fallback: usar el subject si es numérico
            String sub = claims.getSubject();
            if (sub != null) {
                try {
                    return Long.parseLong(sub);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        }
        if (userIdClaim instanceof Number) {
            return ((Number) userIdClaim).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(userIdClaim));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String[] getRoles(String token) {
        try {
            Claims claims = getAllClaims(token);
            Object rolesObj = claims.get("roles");
            if (rolesObj == null) return new String[0];

            // roles puede venir como "ROLE_USER,ROLE_ADMIN" o como lista serializada
            String text = String.valueOf(rolesObj);
            return text.split(",");
        } catch (Exception e) {
            return new String[0];
        }
    }
}