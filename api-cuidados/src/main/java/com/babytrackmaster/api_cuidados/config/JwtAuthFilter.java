package com.babytrackmaster.api_cuidados.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro JWT compatible con Spring Boot 3 (Jakarta) y Java 8 sin lambdas.
 *
 * - Excluye Swagger/Actuator/OPTIONS para no romper /v3/api-docs.
 * - Acepta cabecera Authorization: Bearer <token> (case-insensitive).
 * - Decodifica clave Base64 y valida tokens HS256/HS512 con JJWT 0.11.x.
 * - Crea autoridades con prefijo ROLE_ para usar hasAnyRole("USER",...).
 * - No escribe la respuesta: limpia contexto y deja que Security responda 401.
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final String secret;

    public JwtAuthFilter(String secret) {
        this.secret = secret;
    }

    // >>> NUEVO: no filtrar Swagger/Actuator/Error <<<
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String p = request.getRequestURI();
        if (p == null) return false;
        return p.startsWith("/v3/api-docs")
                || p.startsWith("/swagger-ui")
                || p.startsWith("/actuator")
                || "/error".equals(p);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
            	Claims claims = Jwts.parser().setSigningKey(Decoders.BASE64.decode(secret)).parseClaimsJws(token).getBody();
                String username = claims.getSubject();

                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) claims.get("roles");
                List<SimpleGrantedAuthority> auths = new ArrayList<SimpleGrantedAuthority>();
                if (roles != null) {
                    for (int i = 0; i < roles.size(); i++) {
                        // Importante: ROLE_ + nombre => compatible con hasAnyRole(...)
                        auths.add(new SimpleGrantedAuthority("ROLE_" + roles.get(i)));
                    }
                }
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, auths);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
