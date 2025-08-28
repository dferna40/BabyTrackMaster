package com.babytrackmaster.api_bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_bff.dto.GastoDTO;
import com.babytrackmaster.api_bff.http.ServiceClient;
import com.babytrackmaster.api_bff.security.JwtService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bff/gastos")
@Tag(name = "Gastos BFF")
@RequiredArgsConstructor
public class GastosBffController {

  private final ServiceClient client;
  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<GastoDTO> crear(HttpServletRequest req, @RequestBody GastoDTO gasto) {
    String url = client.gastosUrl("gastos");
    return client.post(req, url, gasto, GastoDTO.class);
  }

  @GetMapping("/mes")
  public ResponseEntity<GastoDTO[]> listarPorMes(HttpServletRequest req,
       @RequestParam String mes) {
	  Long usuarioId = jwtService.resolveUserId();
    String url = client.gastosUrl("gastos/mes?usuarioId=" + usuarioId + "&mes=" + mes);
    return client.get(req, url, GastoDTO[].class);
  }

  // Añade PUT/DELETE según tus MS
}