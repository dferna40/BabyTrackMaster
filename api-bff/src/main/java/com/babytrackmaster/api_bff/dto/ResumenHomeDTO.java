package com.babytrackmaster.api_bff.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumenHomeDTO {
  private List<DiarioDTO> ultimosDiarios;
  private List<GastoDTO> gastosDelMes;
  private List<CitaDTO> proximasCitas;
}