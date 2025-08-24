package com.babytrackmaster.api_cuidados.security;



import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.babytrackmaster.api_cuidados.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro que autentica peticiones usando un JWT en el header Authorization.
 * No depende de extractAllClaims; usa getAllClaims del JwtService.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    String token = header.substring(7).trim(); // después de "Bearer "
    if (!jwtService.isTokenValid(token)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Token requerido o inválido\"}");
      return;
    }

    String username = jwtService.extractUsername(token);
    var authorities = jwtService.extractAuthorities(token); // si no usas roles, deja List.of()

    UsernamePasswordAuthenticationToken auth =
    	    new UsernamePasswordAuthenticationToken(username, null, authorities);
    	auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    	SecurityContextHolder.getContext().setAuthentication(auth);

    chain.doFilter(request, response);
  }
}