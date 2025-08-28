package com.babytrackmaster.api_bff.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.babytrackmaster.api_bff.dto.CitaDTO;
import com.babytrackmaster.api_bff.dto.DiarioDTO;
import com.babytrackmaster.api_bff.dto.GastoDTO;
import com.babytrackmaster.api_bff.dto.ResumenHomeDTO;
import com.babytrackmaster.api_bff.http.ServiceClient;
import com.babytrackmaster.api_bff.service.HomeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

  private final ServiceClient client;

  public ResumenHomeDTO getResumenHome(HttpServletRequest request, Long usuarioId, String mesAnio) {
    List<DiarioDTO> diarios = obtenerUltimosDiarios(request, usuarioId);
    List<GastoDTO> gastos = obtenerGastosDelMes(request, usuarioId, mesAnio);
    List<CitaDTO> citas  = obtenerProximasCitas(request, usuarioId);

    ResumenHomeDTO dto = ResumenHomeDTO.builder().build();
    dto.setUltimosDiarios(diarios);
    dto.setGastosDelMes(gastos);
    dto.setProximasCitas(citas);
    return dto;
  }

  private List<DiarioDTO> obtenerUltimosDiarios(HttpServletRequest req, Long usuarioId) {
    String url = client.diarioUrl("diario/ultimos?usuarioId=" + usuarioId + "&limit=3");
    ResponseEntity<DiarioDTO[]> res = client.get(req, url, DiarioDTO[].class);
    if (res.getBody() == null) return Collections.emptyList();
    return Arrays.asList(res.getBody());
  }

  private List<GastoDTO> obtenerGastosDelMes(HttpServletRequest req, Long usuarioId, String mesAnio) {
    String url = client.gastosUrl("gastos/mes?usuarioId=" + usuarioId + "&mes=" + mesAnio);
    ResponseEntity<GastoDTO[]> res = client.get(req, url, GastoDTO[].class);
    if (res.getBody() == null) return Collections.emptyList();
    return Arrays.asList(res.getBody());
  }

  private List<CitaDTO> obtenerProximasCitas(HttpServletRequest req, Long usuarioId) {
    String url = client.citasUrl("citas/proximas?usuarioId=" + usuarioId + "&limit=3");
    ResponseEntity<CitaDTO[]> res = client.get(req, url, CitaDTO[].class);
    if (res.getBody() == null) return Collections.emptyList();
    return Arrays.asList(res.getBody());
  }
}
