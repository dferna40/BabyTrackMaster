package service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import config.UserIdResolver;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;
    
    private String issuer;
    
    /**
     * Resolver opcional para obtener el userId a partir del username (email/login).
     * Si no hay bean disponible, no pasa nada: el token saldrá sin claim userId (como antes).
     */
    @Autowired(required = false)
    private UserIdResolver userIdResolver;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public String generarToken(String username) {
//        Date ahora = new Date();
//        Date expira = new Date(ahora.getTime() + expirationMs);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(ahora)
//                .setExpiration(expira)
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
    
    /**
     * FIRMA NO CAMBIA: generamos un JWT con sub=username y, si es posible, añadimos claim userId (numérico).
     */
    public String generarToken(String username) {
        Date ahora = new Date();
        long expMillis = ahora.getTime() + (expirationMs * 60L * 1000L);
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)           // mantenemos compatibilidad: sub = username
                .setIssuedAt(ahora)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);

        if (issuer != null && issuer.trim().length() > 0) {
            builder.setIssuer(issuer);         // opcional
        }

        // Intentamos añadir claim userId (numérico)
        Long userId = resolveUserId(username);
        if (userId != null) {
            builder.claim("userId", userId);
        }
        return builder.compact();
    }

    public String extraerUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public boolean esTokenValido(String token) {
        try {
            Claims claims = getAllClaims(token);
            Date exp = claims.getExpiration();
            return exp.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    private Long resolveUserId(String username) {
        // 1) Si username es numérico, úsalo como id
        if (username != null) {
            try {
                return Long.valueOf(username);
            } catch (NumberFormatException ignore) {
                // no es numérico, seguimos
            }
        }
        // 2) Si tenemos un resolver registrado, úsalo
        if (userIdResolver != null) {
            try {
                return userIdResolver.resolve(username);
            } catch (Exception ignore) {
                // si falla, seguimos sin id
            }
        }
        // 3) No pudimos determinar userId
        return null;
    }
}
