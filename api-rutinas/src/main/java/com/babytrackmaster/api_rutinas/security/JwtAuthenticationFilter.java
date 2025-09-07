package com.babytrackmaster.api_rutinas.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // Debes tener este bean en tu proyecto

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.substring(7);

            if (!jwtService.esTokenValido(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            Claims claims = jwtService.getAllClaims(token);
            
            System.out.println("[JWT claims] sub=" + claims.getSubject()
            + " userId=" + claims.get("userId")
            + " usuarioId=" + claims.get("usuarioId")
            + " id=" + claims.get("id")
            + " uid=" + claims.get("uid")
            + " user_id=" + claims.get("user_id"));

            // username (sub) e intento de userId robusto
            String username = claims.getSubject();
            Long userId = null;

            Object c = claims.get("userId");
            if (c == null) { c = claims.get("usuarioId"); }
            if (c == null) { c = claims.get("id"); }

            if (c != null) {
                try {
                    userId = Long.valueOf(String.valueOf(c));
                } catch (NumberFormatException ignore) {}
            }

            if (userId == null && username != null) {
                try {
                    // Por si el sub es directamente el id
                    userId = Long.valueOf(username);
                } catch (NumberFormatException ignore) {}
            }

            // Authorities desde la claim "roles" (lista o CSV)
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            Object rolesClaim = claims.get("roles");

            if (rolesClaim instanceof Collection) {
                Collection col = (Collection) rolesClaim;
                Iterator it = col.iterator();
                while (it.hasNext()) {
                    Object r = it.next();
                    String role = String.valueOf(r);
                    if (role != null && role.length() > 0) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                }
            } else if (rolesClaim != null) {
                String csv = String.valueOf(rolesClaim);
                String[] parts = csv.split(",");
                int i = 0;
                while (i < parts.length) {
                    String role = parts[i] != null ? parts[i].trim() : null;
                    if (role != null && role.length() > 0) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                    i++;
                }
            }

            // Construir principal con getId() y dejar details con "userId"
            UserPrincipal principal = new UserPrincipal(userId, username, null, authorities, true);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);

            Map details = new HashMap();
            if (userId != null) { details.put("userId", userId); }
            if (username != null) { details.put("username", username); }
            auth.setDetails(details);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ex) {
            // No tirar abajo la petición por fallo de parseo; seguimos sin autenticación
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}