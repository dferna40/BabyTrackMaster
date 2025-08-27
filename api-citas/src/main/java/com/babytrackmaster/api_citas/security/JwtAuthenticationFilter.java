package com.babytrackmaster.api_citas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtService.esTokenValido(token)) {
                Claims claims = null;
                try { claims = jwtService.getAllClaims(token); } catch (Exception ignore) { claims = null; }

                Long userId = null;
                String sub = null;

                if (claims != null) {
                    sub = claims.getSubject();
                    Object v = claims.get("userId");
                    if (v == null) v = claims.get("usuarioId");
                    if (v == null) v = claims.get("id");
                    if (v == null) v = claims.get("user_id");
                    if (v != null) {
                        try { userId = Long.valueOf(String.valueOf(v)); } catch (NumberFormatException ignore) {}
                    }
                    if (userId == null && sub != null) {
                        try { userId = Long.valueOf(sub); } catch (NumberFormatException ignore) {}
                    }
                }
                
                System.out.println("[JWT claims] sub=" + claims.getSubject()
                + " userId=" + claims.get("userId")
                + " usuarioId=" + claims.get("usuarioId")
                + " id=" + claims.get("id")
                + " uid=" + claims.get("uid")
                + " user_id=" + claims.get("user_id"));

                // Si NO hay userId numérico → no autenticamos (forzará 401 por Security)
                if (userId != null) {
                    List<SimpleGrantedAuthority> auths = new ArrayList<SimpleGrantedAuthority>();
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userId, null, auths);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}