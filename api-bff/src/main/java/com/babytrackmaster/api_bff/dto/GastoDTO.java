package com.babytrackmaster.api_bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GastoDTO {
  private Long id;
  private String categoria;
  private String descripcion;
  private Double importe;
  private String fecha; // yyyy-MM-dd
}
